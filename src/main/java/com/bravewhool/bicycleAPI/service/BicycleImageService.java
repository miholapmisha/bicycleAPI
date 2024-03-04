package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.entity.BicycleImage;
import com.bravewhool.bicycleAPI.exception.BicycleImageStorageException;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.repository.BicycleImageRepository;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BicycleImageService {

    private static final List<String> AVAILABLE_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "bmp");

    @Value("${filesystem.storage.folder}")
    private String filesystemStorageFolder;

    private final ResourceLoader resourceLoader;

    private final BicycleImageRepository bicycleImageRepository;

    private final BicycleRepository bicycleRepository;

    @Transactional
    public String uploadBicycleImage(MultipartFile image, Long bicycleId) {

        if (image == null || image.isEmpty() || image.getOriginalFilename() == null)
            throw new BicycleImageStorageException("Uploaded file is empty or with missing data!");


        String imageName = image.getOriginalFilename();
        String imageExtension = FilenameUtils.getExtension(imageName);

        if (!AVAILABLE_IMAGE_EXTENSIONS.contains(imageExtension))
            throw new BicycleImageStorageException("File with unsupported extensions: ." + imageExtension + "!");

        saveToDataBase(bicycleId, imageName);
        writeToFileSystem(image);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(imageName)
                .toUriString();
    }

    @Transactional
    public void saveToDataBase(Long bicycleId, String imageName) {

        BicycleImage bicycleImage = new BicycleImage();
        bicycleImage.setName(imageName);
        bicycleImage.setBicycle(bicycleRepository.findById(bicycleId)
                .orElseThrow(() -> new BicycleNotFoundException(bicycleId)));

        bicycleImageRepository.save(bicycleImage);
    }

    private void writeToFileSystem(MultipartFile image) {

        try {

            Resource resource = resourceLoader.getResource(String.format("classpath:%s", filesystemStorageFolder));
            String externalFolderPath = resource.getFile().getAbsolutePath();
            File file = new File(externalFolderPath + File.separator + image.getOriginalFilename());

            OutputStream os = new FileOutputStream(file);
            os.write(image.getBytes());
            os.close();

        } catch (IOException e) {
            throw new BicycleImageStorageException(e.getMessage());
        }

    }

}

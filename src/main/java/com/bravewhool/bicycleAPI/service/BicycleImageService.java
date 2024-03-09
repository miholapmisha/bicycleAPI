package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.entity.BicycleImage;
import com.bravewhool.bicycleAPI.exception.BicycleImageStorageException;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.repository.BicycleImageRepository;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BicycleImageService {

    private static final List<String> AVAILABLE_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "bmp");

    @Value("${filesystem.storage.folder}")
    private String filesystemStorageFolder;

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

            Path externalPath = Path.of(filesystemStorageFolder);

            if (!Files.exists(externalPath)) {
                Files.createDirectories(externalPath);
            }

            Path filePath = externalPath.resolve(Objects.requireNonNull(image.getOriginalFilename()));
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}

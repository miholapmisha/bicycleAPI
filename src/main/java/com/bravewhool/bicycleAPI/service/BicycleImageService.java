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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BicycleImageService {

    private static final List<String> AVAILABLE_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "bmp");

    @Value("${filesystem.storage.folder}")
    private String filesystemStorageFolder;

    private final BicycleImageRepository bicycleImageRepository;

    private final BicycleRepository bicycleRepository;

    @Transactional
    public String uploadBicycleImage(MultipartFile image, UUID bicycleId) {
        try {

            if (image == null || image.isEmpty() || image.getOriginalFilename() == null)
                throw new BicycleImageStorageException("Uploaded file is empty or with missing data!");


            String imageName = image.getOriginalFilename();
            String imageExtension = FilenameUtils.getExtension(imageName);

            if (!AVAILABLE_IMAGE_EXTENSIONS.contains(imageExtension))
                throw new BicycleImageStorageException("File with unsupported extensions: ." + imageExtension + "!");

            Path externalPath = Path.of(filesystemStorageFolder);
            if (!Files.exists(externalPath)) {
                Files.createDirectories(externalPath);
            }
            Path fileToSave = Path.of(filesystemStorageFolder).resolve(image.getOriginalFilename());
            if (fileToSave.toFile().exists())
                throw new BicycleImageStorageException("File with such name already exist!");

            saveToDataBase(bicycleId, imageName);
            Files.copy(image.getInputStream(), fileToSave, StandardCopyOption.REPLACE_EXISTING);

            return imageName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveToDataBase(UUID bicycleId, String imageName) {

        BicycleImage bicycleImage = new BicycleImage();
        bicycleImage.setName(imageName);
        bicycleImage.setBicycle(bicycleRepository.findById(bicycleId)
                .orElseThrow(() -> new BicycleNotFoundException(bicycleId)));

        bicycleImageRepository.save(bicycleImage);
    }

}

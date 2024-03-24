package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.entity.BicycleImage;
import com.bravewhool.bicycleAPI.exception.BicycleImageStorageException;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.models.Base64Image;
import com.bravewhool.bicycleAPI.repository.BicycleImageRepository;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public String uploadBicycleImage(Base64Image imageFile, UUID bicycleId) {
        try {

            if (imageFile == null)
                throw new BicycleImageStorageException("Image file does not exist!");

            String base64Data = imageFile.getBase64Data();
            String imageName = imageFile.getName();
            String imageExtension = FilenameUtils.getExtension(imageName);
            if (!AVAILABLE_IMAGE_EXTENSIONS.contains(imageExtension))
                throw new BicycleImageStorageException("File with unsupported extension: ." + imageExtension + "!");

            Path externalPath = Path.of(filesystemStorageFolder);
            if (!Files.exists(externalPath))
                Files.createDirectories(externalPath);

            Path fileToSave = externalPath.resolve(imageName);
            if (Files.exists(fileToSave))
                throw new BicycleImageStorageException("File with such name already exists: " + imageName);

            saveToDataBase(bicycleId, imageName);

            try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileToSave.toFile()))) {
                outputStream.write(DatatypeConverter.parseBase64Binary(base64Data));
            }

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

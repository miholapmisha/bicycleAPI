package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.entity.BicycleImage;
import com.bravewhool.bicycleAPI.exception.BicycleImageNotFoundException;
import com.bravewhool.bicycleAPI.exception.BicycleImageStorageException;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.repository.BicycleImageRepository;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BicycleImageService {

    private final BicycleImageRepository bicycleImageRepository;

    private final BicycleRepository bicycleRepository;

    private final Cloudinary cloudinary;

    @Transactional
    public List<String> uploadBicycleImages(List<String> base64Images, UUID bicycleId) {
        Bicycle bicycle = bicycleRepository.findById(bicycleId)
                .orElseThrow(() -> new BicycleNotFoundException(bicycleId));

        return base64Images.stream()
                .map(base64Image -> uploadBicycleImage(base64Image, bicycle))
                .toList();
    }

    @Transactional
    public String uploadBicycleImage(String base64Image, Bicycle bicycle) {

        try {
            if (base64Image == null)
                throw new BicycleImageStorageException("Image data does not exist!");

            byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            if (image == null)
                throw new BicycleImageStorageException("Failed to convert data into string");

            var uploadResult = cloudinary.uploader().upload(decodedBytes, ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            String publicId = (String) uploadResult.get("public_id");
            saveToDataBase(bicycle, url, publicId);

            return url;
        } catch (IOException | IllegalArgumentException e) {
            throw new BicycleImageStorageException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void saveToDataBase(Bicycle bicycle, String imageUrl, String publicId) {
        BicycleImage bicycleImage = new BicycleImage();
        bicycleImage.setId(publicId);
        bicycleImage.setUrl(imageUrl);
        bicycleImage.setBicycle(bicycle);

        bicycleImageRepository.save(bicycleImage);
    }

    @Transactional
    public void removeImageByUrl(String url) {

        try {
            BicycleImage bicycleImage = bicycleImageRepository.getBicycleImageByUrl(url)
                    .orElseThrow(() -> new BicycleImageNotFoundException("url: " + url));
            String publicId = bicycleImage.getId();
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            bicycleImageRepository.removeByUrl(url);
        } catch (IOException | IllegalArgumentException e) {
            throw new BicycleImageStorageException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void removeImagesByBicycleId(UUID bicycleId) {
        Bicycle bicycle = bicycleRepository.findById(bicycleId)
                .orElseThrow(() -> new BicycleNotFoundException(bicycleId));
        removeImagesFromStorageByBicycleId(bicycle.getId());
        bicycleImageRepository.removeByBicycleId(bicycle.getId());
    }

    public void removeImagesFromStorageByBicycleId(UUID bicycleId) {

        try {
            List<BicycleImage> bicycleImages = bicycleImageRepository.getBicycleImagesByBicycleId(bicycleId);
            if (bicycleImages == null)
                throw new BicycleImageNotFoundException("bicycle id:" + bicycleId);
            for (BicycleImage bicycleImage : bicycleImages) {
                cloudinary.uploader().destroy(bicycleImage.getId(), ObjectUtils.emptyMap());
            }
        } catch (IOException | IllegalArgumentException e) {
            throw new BicycleImageStorageException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getUrlsByBicycleId(UUID bicycleId) {
        return bicycleImageRepository.getImageUrlsByBicycleId(bicycleId);
    }

}

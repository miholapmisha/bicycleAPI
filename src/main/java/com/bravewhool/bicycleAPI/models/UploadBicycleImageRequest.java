package com.bravewhool.bicycleAPI.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadBicycleImageRequest {

    private Long bicycleId;

    private MultipartFile image;
}

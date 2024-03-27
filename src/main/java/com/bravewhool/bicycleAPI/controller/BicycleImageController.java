package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.models.BicycleImageRequest;
import com.bravewhool.bicycleAPI.service.BicycleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bicycle/images")
@RequiredArgsConstructor
public class BicycleImageController {

    private final BicycleImageService bicycleImageService;

    @GetMapping("/{uuid}")
    public List<String> getImagesByBicycleId(@PathVariable UUID uuid) {
        return bicycleImageService.getUrlsByBicycleId(uuid);
    }

    @PostMapping("/save")
    public List<String> uploadImage(@RequestBody BicycleImageRequest request) {
        return bicycleImageService.uploadBicycleImages(request.getImages(), request.getBicycleId());
    }

    @DeleteMapping("/delete/byBicycleUrl")
    public void deleteImagesByBicycleUrl(@RequestParam String url) {
        bicycleImageService.removeImageByUrl(url);
    }

    @DeleteMapping("/delete/byBicycleId")
    public void deleteImagesByBicycleUrl(@RequestParam UUID uuid) {
        bicycleImageService.removeImagesByBicycleId(uuid);
    }
}

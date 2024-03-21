package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.models.BicycleUpdateRequest;
import com.bravewhool.bicycleAPI.service.BicycleImageService;
import com.bravewhool.bicycleAPI.service.BicycleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/bicycle")
@RequiredArgsConstructor
public class BicycleController {

    private final BicycleService bicycleService;

    private final BicycleImageService bicycleImageService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile image, @RequestParam UUID bicycleId) {
        return ResponseEntity.ok(bicycleImageService.uploadBicycleImage(image, bicycleId));
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BicycleDTO> saveBicycle(
            @RequestPart(value = "bicycleUpdateRequest")
            @Parameter(schema = @Schema(type = "string", format = "binary")) @Validated BicycleUpdateRequest bicycleUpdateRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images)  {

        return images != null && !images.isEmpty()
                ? ResponseEntity.ok(bicycleService.saveBicycleWithImages(bicycleUpdateRequest, images))
                : ResponseEntity.ok(bicycleService.saveBicycle(bicycleUpdateRequest));
    }

    @GetMapping("/page-request")
    public Page<BicycleDTO> getBicyclesByPageRequest(@RequestParam int size, @RequestParam int page) {
        return bicycleService.getBicyclesByPageRequest(size, page);
    }

    @GetMapping("/all")
    public List<BicycleDTO> searchBicyclesByIds(@RequestBody List<UUID> ids) {
        return bicycleService.findBicyclesByIds(ids);
    }

    @GetMapping("/{id}")
    public BicycleDTO findBicyclesById(@PathVariable UUID id) {
        return bicycleService.findBicyclesById(id);
    }

    @GetMapping("/filter")
    public List<BicycleDTO> searchBicyclesByFilters(@RequestBody Map<String, Object> searchRequest) {
        return bicycleService.findBicyclesBySearchRequest(searchRequest);
    }

    @GetMapping("/search")
    public List<BicycleDTO> searchBicyclesByName(@RequestParam String input) {
        return bicycleService.getBicyclesLikeName(input);
    }

    @GetMapping("/find")
    public List<BicycleDTO> findBicyclesByName(@RequestParam String input) {
        return bicycleService.getBicyclesByName(input);
    }

    @GetMapping("/colors")
    public Set<String> getBicyclesColors() {
        return bicycleService.getUsedBicycleColors();
    }

    @PutMapping("/update/{id}")
    public void updateBicycle(@RequestBody @Validated BicycleUpdateRequest request, @PathVariable UUID id) {
        bicycleService.updateBicycle(request, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBicycle(@PathVariable UUID id) {
        bicycleService.deleteBicycle(id);
    }
}

package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.models.Base64Image;
import com.bravewhool.bicycleAPI.models.BicycleUpdateRequest;
import com.bravewhool.bicycleAPI.service.BicycleImageService;
import com.bravewhool.bicycleAPI.service.BicycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> uploadFile(@RequestParam Base64Image image, @RequestParam UUID bicycleId) {
        return ResponseEntity.ok(bicycleImageService.uploadBicycleImage(image, bicycleId));
    }

    @PostMapping(value = "/save")
    public BicycleDTO saveBicycle(@RequestBody BicycleUpdateRequest bicycleUpdateRequest) {
        return bicycleService.saveBicycleWithImages(bicycleUpdateRequest);
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

package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.models.BicycleBaseImageRequest;
import com.bravewhool.bicycleAPI.models.BicycleBaseRequest;
import com.bravewhool.bicycleAPI.service.BicycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/bicycle")
@RequiredArgsConstructor
public class BicycleController {

    private final BicycleService bicycleService;

    @PostMapping(value = "/save")
    public BicycleDTO saveBicycle(@RequestBody BicycleBaseImageRequest bicycleBaseRequest) {
        return bicycleService.saveBicycleWithImages(bicycleBaseRequest);
    }

    @GetMapping("/page-request")
    public Page<BicycleDTO> getBicyclesByPageRequest(@RequestParam int size, @RequestParam int page) {
        return bicycleService.getBicyclesByPageRequest(size, page);
    }

    @GetMapping(value = "/all")
    public List<BicycleDTO> findBicyclesByIds(@RequestParam List<UUID> ids) {
        return bicycleService.findBicyclesByIds(ids);
    }

    @GetMapping("/{id}")
    public BicycleDTO findBicyclesById(@PathVariable UUID id) {
        return bicycleService.findBicyclesById(id);
    }

    @GetMapping("/filter")
    public List<BicycleDTO> searchBicyclesByFilters(@RequestParam MultiValueMap<String, String> searchRequest) {
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
    public BicycleDTO updateBicycle(@RequestBody @Validated BicycleBaseRequest request, @PathVariable UUID id) {
        return bicycleService.updateBicycle(request, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBicycle(@PathVariable UUID id) {
        bicycleService.deleteBicycle(id);
    }
}

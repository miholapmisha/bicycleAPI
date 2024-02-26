package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.models.UpdateBicycleRequest;
import com.bravewhool.bicycleAPI.service.BicycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bicycle")
@RequiredArgsConstructor
public class BicycleController {

    private final BicycleService bicycleService;

    @GetMapping("/page-request")
    public Page<BicycleDTO> getBicyclesByPageRequest(@RequestParam int size, @RequestParam int page) {
        return bicycleService.getBicyclesByPageRequest(size, page);
    }

    @GetMapping("/all")
    public List<BicycleDTO> searchBicyclesByIds(@RequestBody List<Long> ids) {
        return bicycleService.findBicyclesByIds(ids);
    }

    @GetMapping("/{id}")
    public BicycleDTO findBicyclesById(@PathVariable Long id) {
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

    @PutMapping("/{id}")
    public void updateBicycle(@RequestBody @Validated UpdateBicycleRequest request, @PathVariable Long id) {
        bicycleService.updateBicycle(request, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBicycle(@PathVariable Long id) {
        bicycleService.deleteBicycle(id);
    }
}

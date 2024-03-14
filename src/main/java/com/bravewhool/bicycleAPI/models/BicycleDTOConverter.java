package com.bravewhool.bicycleAPI.models;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.entity.BicycleImage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BicycleDTOConverter {

    private final ModelMapper modelMapper;

    public BicycleDTO convertToDTO(Bicycle entity) {

        BicycleDTO bicycleDTO = modelMapper.map(entity, BicycleDTO.class);

        Set<String> imageUrls = entity.getImages()
                .stream()
                .map(BicycleImage::getName)
                .collect(Collectors.toSet());

        bicycleDTO.setImageNames(imageUrls);

        return bicycleDTO;

    }

    public List<BicycleDTO> convertToDTO(List<Bicycle> bicycles) {
        return bicycles.stream()
                .map(this::convertToDTO)
                .toList();

    }


}

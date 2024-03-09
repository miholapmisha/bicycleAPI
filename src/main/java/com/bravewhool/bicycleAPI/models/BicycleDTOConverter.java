package com.bravewhool.bicycleAPI.models;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BicycleDTOConverter {

    private final ModelMapper modelMapper;

    public BicycleDTO convertToDTO(Bicycle entity) {

        BicycleDTO bicycleDTO = modelMapper.map(entity, BicycleDTO.class);

        List<String> imageUrls = entity.getImages()
                .stream()
                .map(item -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(item.getName())
                        .toUriString())
                .toList();

        bicycleDTO.setImagesUrls(imageUrls);

        return bicycleDTO;

    }

    public List<BicycleDTO> convertToDTO(List<Bicycle> bicycles) {
        return bicycles.stream()
                .map(this::convertToDTO)
                .toList();

    }


}

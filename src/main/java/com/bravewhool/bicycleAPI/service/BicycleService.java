package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.models.Base64Image;
import com.bravewhool.bicycleAPI.models.BicycleDTOConverter;
import com.bravewhool.bicycleAPI.models.BicycleUpdateRequest;
import com.bravewhool.bicycleAPI.models.specification.EntitySearchCriteria;
import com.bravewhool.bicycleAPI.models.specification.SearchOperator;
import com.bravewhool.bicycleAPI.models.specification.SearchSpecification;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BicycleService {

    private final BicycleRepository bicycleRepository;

    private final SearchSpecification<Bicycle> searchSpecification;

    private final BicycleDTOConverter bicycleEntityConverter;

    private final ModelMapper mapper;

    private final BicycleImageService bicycleImageService;

    public List<BicycleDTO> getBicyclesLikeName(String name) {
        return bicycleEntityConverter.convertToDTO(bicycleRepository.findByNameContainingIgnoreCase(name));
    }

    public List<BicycleDTO> getBicyclesByName(String name) {
        return bicycleEntityConverter.convertToDTO(bicycleRepository.findBicycleByName(name));
    }

    public Page<BicycleDTO> getBicyclesByPageRequest(int size, int page) {
        return bicycleRepository.findAll(PageRequest.of(page, size))
                .map(bicycleEntityConverter::convertToDTO);
    }

    @Transactional
    public void updateBicycle(BicycleUpdateRequest request, UUID id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        mapper.map(request, bicycle);
        bicycle.setImages(new ArrayList<>());
        bicycleRepository.save(bicycle);

    }

    @Transactional
    public BicycleDTO saveBicycleWithImages(BicycleUpdateRequest request) {
        BicycleDTO bicycleDTO = saveBicycle(request);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> imageNames = new ArrayList<>();
            for (Base64Image image : request.getImages()) {
                imageNames.add(bicycleImageService.uploadBicycleImage(image, UUID.fromString(bicycleDTO.getId())));
            }
            bicycleDTO.setImageNames(imageNames);
        }

        return bicycleDTO;
    }

    @Transactional
    public BicycleDTO saveBicycle(BicycleUpdateRequest request) {
        Bicycle bicycle = new Bicycle();

        mapper.map(request, bicycle);
        bicycle.setImages(new ArrayList<>());
        bicycleRepository.save(bicycle);

        return bicycleEntityConverter.convertToDTO(bicycle);
    }

    @Transactional
    public void deleteBicycle(UUID id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        bicycleRepository.removeById(bicycle.getId());
    }

    public List<BicycleDTO> findBicyclesByIds(List<UUID> ids) {
        return ids.stream().map(this::findBicyclesById).toList();
    }

    public BicycleDTO findBicyclesById(UUID id) {

        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        return bicycleEntityConverter.convertToDTO(bicycle);
    }

    public List<BicycleDTO> findBicyclesBySearchRequest(Map<String, Object> searchRequest) {
        Map<String, List<EntitySearchCriteria>> fieldNameToCriteriaMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : searchRequest.entrySet()) {

            String fieldName = entry.getKey();
            switch (fieldName) {
                case "search" -> fieldNameToCriteriaMap.put("name",
                        List.of(new EntitySearchCriteria("name", entry.getValue(), SearchOperator.I_LIKE)));
                case "bicycleType", "materialType", "color", "wheelSize", "frameType" -> {

                    List<?> values = (List<?>) entry.getValue();

                    fieldNameToCriteriaMap.put(fieldName, values.stream()
                            .map(item -> new EntitySearchCriteria(fieldName, item.toString().toUpperCase(), SearchOperator.EQUALS))
                            .toList());

                }
                case "lowerBoundPrice" -> fieldNameToCriteriaMap.put("lowerBoundPrice",
                        List.of(new EntitySearchCriteria("price", entry.getValue(), SearchOperator.GREATER_THAN)));
                case "upperBoundPrice" -> fieldNameToCriteriaMap.put("lowerBoundPrice",
                        List.of(new EntitySearchCriteria("price", entry.getValue(), SearchOperator.LESS_THAN)));
                case "sale" -> {
                    Boolean isThereSale = (Boolean) entry.getValue();
                    if (isThereSale)
                        fieldNameToCriteriaMap.put("sale", List.of(new EntitySearchCriteria("sale", entry.getValue(), SearchOperator.IS_TRUE)));
                    else
                        fieldNameToCriteriaMap.put("sale", List.of(new EntitySearchCriteria("sale", entry.getValue(), SearchOperator.IS_FALSE)));
                }
            }
        }

        List<Bicycle> searchBicycles = bicycleRepository.findAll(searchSpecification.getFinalSpecification(fieldNameToCriteriaMap));

        return bicycleEntityConverter.convertToDTO(searchBicycles);
    }

    public Set<String> getUsedBicycleColors() {
        return bicycleRepository.getUniqueColors();
    }

}
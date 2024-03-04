package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.models.BicycleDTOConverter;
import com.bravewhool.bicycleAPI.models.BicycleUpdateRequest;
import com.bravewhool.bicycleAPI.models.specification.EntitySearchCriteria;
import com.bravewhool.bicycleAPI.models.specification.SearchOperator;
import com.bravewhool.bicycleAPI.models.specification.SearchSpecification;
import com.bravewhool.bicycleAPI.repository.BicycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BicycleService {

    private final BicycleRepository bicycleRepository;

    private final SearchSpecification<Bicycle> searchSpecification;

    private final BicycleDTOConverter bicycleEntityConverter;

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
    public void updateBicycle(BicycleUpdateRequest request, Long id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        assignPropertiesFromRequest(bicycle, request);
        bicycleRepository.save(bicycle);

    }

    @Transactional
    public BicycleDTO saveBicycle(BicycleUpdateRequest request) {

        Bicycle bicycle = new Bicycle();

        assignPropertiesFromRequest(bicycle, request);
        bicycleRepository.save(bicycle);

        return bicycleEntityConverter.convertToDTO(bicycle);
    }

    private void assignPropertiesFromRequest(Bicycle bicycle, BicycleUpdateRequest request) {

        bicycle.setBicycleType(request.getBicycleType());
        bicycle.setName(request.getName());
        bicycle.setColor(request.getColor());
        bicycle.setSale(request.isSale());
        bicycle.setPrice(request.getPrice());
        bicycle.setMaterialType(request.getMaterialType());
        bicycle.setWheelSize(request.getWheelSize());
    }

    @Transactional
    public void deleteBicycle(Long id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        bicycleRepository.removeById(bicycle.getId());
    }

    public List<BicycleDTO> findBicyclesByIds(List<Long> ids) {
        return ids.stream().map(this::findBicyclesById).toList();
    }

    public BicycleDTO findBicyclesById(Long id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        return bicycleEntityConverter.convertToDTO(bicycle);
    }

    public List<BicycleDTO> findBicyclesBySearchRequest(Map<String, Object> searchRequest) {

        Map<String, List<EntitySearchCriteria>> fieldNameToCriteriaMap = new HashMap<>();


        for (Map.Entry<String, Object> entry : searchRequest.entrySet()) {

            String fieldName = entry.getKey();
            switch (fieldName) {
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
                    Boolean isTrue = (Boolean) entry.getValue();
                    if (isTrue)
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
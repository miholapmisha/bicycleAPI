package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.models.UpdateBicycleRequest;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BicycleService {

    private final BicycleRepository bicycleRepository;

    private final SearchSpecification<Bicycle> searchSpecification;

    private final ModelMapper modelMapper;

    public List<BicycleDTO> getBicyclesLikeName(String name) {
        return convertToDTO(bicycleRepository.findByNameContainingIgnoreCase(name));
    }

    public List<BicycleDTO> getBicyclesByName(String name) {
        return convertToDTO(bicycleRepository.findBicycleByName(name));
    }

    public Page<BicycleDTO> getBicyclesByPageRequest(int size, int page) {

        return bicycleRepository.findAll(PageRequest.of(page, size))
                .map(entity -> modelMapper.map(entity, BicycleDTO.class));
    }

    @Transactional
    public void updateBicycle(UpdateBicycleRequest request, Long id) {
        Optional<Bicycle> bicycle = bicycleRepository.findById(id);

        bicycle.ifPresent(value -> {
            value.setBicycleType(request.getBicycleType());
            value.setName(request.getName());
            value.setColor(request.getColor());
            value.setSale(request.isSale());
            value.setPrice(request.getPrice());
            value.setMaterialType(request.getMaterialType());
            value.setWheelSize(request.getWheelSize());

            bicycleRepository.save(value);
        });

    }

    @Transactional
    public void deleteBicycle(Long id) {
        Optional<Bicycle> bicycle = bicycleRepository.findById(id);
        bicycle.ifPresent(value -> bicycleRepository.removeById(value.getId()));
    }

    public List<BicycleDTO> findBicyclesByIds(List<Long> list) {
        return list.stream().map(this::findBicyclesById).toList();
    }

    public BicycleDTO findBicyclesById(Long id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));

        return modelMapper.map(bicycle, BicycleDTO.class);
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
                case "lowerBoundPrice" ->
                        fieldNameToCriteriaMap.put("lowerBoundPrice",
                                List.of(new EntitySearchCriteria("price", entry.getValue(), SearchOperator.GREATER_THAN)));
                case "upperBoundPrice" ->
                        fieldNameToCriteriaMap.put("lowerBoundPrice",
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

        return convertToDTO(searchBicycles);
    }

    private List<BicycleDTO> convertToDTO(List<Bicycle> bicycles) {
        return bicycles.stream()
                .map(entity -> modelMapper.map(entity, BicycleDTO.class))
                .toList();
    }

}


package com.bravewhool.bicycleAPI.service;

import com.bravewhool.bicycleAPI.dto.BicycleDTO;
import com.bravewhool.bicycleAPI.entity.Bicycle;
import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import com.bravewhool.bicycleAPI.models.BicycleBaseImageRequest;
import com.bravewhool.bicycleAPI.models.BicycleBaseRequest;
import com.bravewhool.bicycleAPI.models.BicycleDTOConverter;
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
import org.springframework.util.MultiValueMap;

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
    public BicycleDTO updateBicycle(BicycleBaseRequest request, UUID id) {
        Bicycle bicycle = bicycleRepository.findById(id)
                .orElseThrow(() -> new BicycleNotFoundException(id));
        mapper.map(request, bicycle);

        return bicycleEntityConverter.convertToDTO(bicycle);
    }

    @Transactional
    public BicycleDTO saveBicycleWithImages(BicycleBaseImageRequest request) {
        BicycleDTO bicycleDTO = saveBicycle(request);

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            Bicycle bicycle = bicycleRepository.findById(bicycleDTO.getId())
                    .orElseThrow(() -> new BicycleNotFoundException(bicycleDTO.getId()));

            List<String> imagesUrls = new ArrayList<>();
            for (String image : request.getImages()) {
                imagesUrls.add(bicycleImageService.uploadBicycleImage(image, bicycle));
            }
            bicycleDTO.setImagesUrls(imagesUrls);
        }

        return bicycleDTO;
    }

    @Transactional
    public BicycleDTO saveBicycle(BicycleBaseRequest request) {
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
        bicycleImageService.removeImagesByBicycleId(id);
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

    public List<BicycleDTO> findBicyclesBySearchRequest(MultiValueMap<String, String> searchRequest) {
        Map<String, List<EntitySearchCriteria>> fieldNameToCriteriaMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : searchRequest.entrySet()) {
            String fieldName = entry.getKey();
            List<String> values = entry.getValue();
            switch (fieldName) {
                case "search" -> fieldNameToCriteriaMap.put("name",
                        List.of(new EntitySearchCriteria("name", values.get(0), SearchOperator.I_LIKE)));
                case "bicycleType", "materialType", "color", "wheelSize", "frameType" ->
                        fieldNameToCriteriaMap.put(fieldName, values.stream()
                                .map(item -> new EntitySearchCriteria(fieldName, item.toUpperCase(), SearchOperator.EQUALS))
                                .toList());
                case "lowerBoundPrice" -> fieldNameToCriteriaMap.put("lowerBoundPrice",
                        List.of(new EntitySearchCriteria("price", values, SearchOperator.GREATER_THAN)));
                case "upperBoundPrice" -> fieldNameToCriteriaMap.put("lowerBoundPrice",
                        List.of(new EntitySearchCriteria("price", values, SearchOperator.LESS_THAN)));
                case "sale" -> {

                    boolean isThereSale = Boolean.parseBoolean(values.get(0));
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
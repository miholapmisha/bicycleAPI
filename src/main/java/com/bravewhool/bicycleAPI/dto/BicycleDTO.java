package com.bravewhool.bicycleAPI.dto;

import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BicycleDTO {

    private String id;

    private String name;

    private BicycleType bicycleType;

    private MaterialType materialType;

    private BicycleFrameType frameType;

    private boolean sale;

    private BigDecimal price;

    private BigDecimal wheelSize;

    private String color;

    private List<String> imageNames;

}

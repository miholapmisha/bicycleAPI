package com.bravewhool.bicycleAPI.dto;

import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class BicycleDTO {

    private UUID id;

    private String name;

    private BicycleType bicycleType;

    private MaterialType materialType;

    private BicycleFrameType frameType;

    private boolean sale;

    private BigDecimal price;

    private BigDecimal wheelSize;

    private String color;

    private String description;

    private BigDecimal weight;

    private Integer guarantee;

    private String brakeType;

    private String brand;

    private Long quantity;

    private List<String> images;
}

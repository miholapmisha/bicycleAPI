package com.bravewhool.bicycleAPI.dto;

import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import lombok.Data;

import java.awt.*;
import java.math.BigDecimal;

@Data
public class BicycleDTO {

    private String name;

    private BicycleType bicycleType;

    private MaterialType materialType;

    private BicycleFrameType frameType;

    private boolean sale;

    private BigDecimal price;

    private BigDecimal wheelSize;

    private String color;

}

package com.bravewhool.bicycleAPI.models;


import com.bravewhool.bicycleAPI.entity.enums.BicycleFrameType;
import com.bravewhool.bicycleAPI.entity.enums.BicycleType;
import com.bravewhool.bicycleAPI.entity.enums.MaterialType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class BicycleUpdateRequest {

    @NotBlank(message = "Bicycle request exception - invalid name: name is empty")
    private String name;

    @NotNull(message = "Bicycle request exception - invalid bicycle type: null")
    private BicycleType bicycleType;

    @NotNull(message = "Bicycle request exception - invalid material type: null")
    private MaterialType materialType;

    @NotNull(message = "Bicycle request exception - invalid bicycle frame type: null")
    private BicycleFrameType frameType;

    private boolean sale;

    @NotNull(message = "Bicycle request exception - invalid price: price is null")
    @Min(value = 0, message = "Bicycle request exception - invalid price: must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Bicycle request exception - invalid wheel size: size is null")
    private BigDecimal wheelSize;

    @NotBlank(message = "Bicycle request exception - invalid color: color is empty")
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Bicycle request exception - invalid color pattern: must be hexadecimal")
    private String color;

}

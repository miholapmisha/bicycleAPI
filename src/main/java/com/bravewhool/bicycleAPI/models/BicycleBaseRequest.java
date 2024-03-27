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
public class BicycleBaseRequest {

    @NotBlank(message = "Bicycle request exception - invalid name: name is empty")
    private String name;

    @NotNull(message = "Bicycle request exception - invalid bicycle type: null")
    private BicycleType bicycleType;

    @NotNull(message = "Bicycle request exception - invalid material type: null")
    private MaterialType materialType;

    @NotNull(message = "Bicycle request exception - invalid bicycle frame type: null")
    private BicycleFrameType frameType;

    @NotNull(message = "Bicycle request exception - invalid price: price is null")
    @Min(value = 0, message = "Bicycle request exception - invalid price: must be greater or equal to 0")
    private BigDecimal price;

    @NotNull(message = "Bicycle request exception - invalid wheel size: size is null")
    private BigDecimal wheelSize;

    @NotBlank(message = "Bicycle request exception - invalid color: color is empty")
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "Bicycle request exception - invalid color pattern: must be hexadecimal")
    private String color;

    @NotNull(message = "Bicycle request exception - invalid description: description is null")
    private String description;

    @NotNull(message = "Bicycle request exception - invalid brand: brand is null")
    private String brand;

    @NotNull(message = "Bicycle request exception - invalid brake type: brake type is null")
    private String brakeType;

    @NotNull(message = "Bicycle request exception - invalid wheel weight: weight is null")
    @Min(value = 0, message = "Bicycle request exception - invalid weight: must be greater or equal to 0")
    private BigDecimal weight;

    @NotNull(message = "Bicycle request exception - invalid guarantee value: guarantee value is null")
    @Min(value = 0, message = "Bicycle request exception - invalid guarantee value: must be greater or equal to 0")
    private Integer guarantee;

    @NotNull(message = "Bicycle request exception - invalid quantity value: quantity value is null")
    @Min(value = 0, message = "Bicycle request exception - invalid quantity value: must be greater or equal to 0")
    private Long quantity;

    private boolean sale;

}

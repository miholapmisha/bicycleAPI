package com.bravewhool.bicycleAPI.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BicycleBaseImageRequest extends BicycleBaseRequest {

    private List<String> images;
}

package com.bravewhool.bicycleAPI.models;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BicycleImageRequest {

    private UUID bicycleId;

    private List<String> images;
}

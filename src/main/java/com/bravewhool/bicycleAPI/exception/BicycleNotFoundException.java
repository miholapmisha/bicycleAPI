package com.bravewhool.bicycleAPI.exception;

import java.util.UUID;

public class BicycleNotFoundException extends NotFoundException {

    public BicycleNotFoundException(UUID id) {
        super("Bicycle not found with id: " + id);
    }
}

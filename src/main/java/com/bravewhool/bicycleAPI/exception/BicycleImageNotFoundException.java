package com.bravewhool.bicycleAPI.exception;

public class BicycleImageNotFoundException extends NotFoundException {

    public BicycleImageNotFoundException(String message) {
        super("Bicycle image not found by " + message);
    }
}

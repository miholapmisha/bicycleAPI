package com.bravewhool.bicycleAPI.exception;
public class BicycleNotFoundException extends RuntimeException{

    public BicycleNotFoundException(Long id) {
        super("Bicycle not found with id: " + id);
    }
}

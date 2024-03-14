package com.bravewhool.bicycleAPI.controller.advice;

import com.bravewhool.bicycleAPI.exception.BicycleImageStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class BicycleImageStorageAdvice {

    @ExceptionHandler(BicycleImageStorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleImageStoreException(BicycleImageStorageException exception) {
        return Map.of("message", exception.getMessage());
    }
}

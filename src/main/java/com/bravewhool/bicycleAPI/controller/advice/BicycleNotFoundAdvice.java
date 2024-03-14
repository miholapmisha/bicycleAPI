package com.bravewhool.bicycleAPI.controller.advice;

import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class BicycleNotFoundAdvice {

    @ExceptionHandler(BicycleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleBicycleNotFoundException(BicycleNotFoundException exception) {
        return Map.of("message", exception.getMessage());
    }
}

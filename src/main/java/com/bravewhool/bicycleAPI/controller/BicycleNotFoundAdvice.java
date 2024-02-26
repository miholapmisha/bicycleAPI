package com.bravewhool.bicycleAPI.controller;

import com.bravewhool.bicycleAPI.exception.BicycleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BicycleNotFoundAdvice {

    @ExceptionHandler(BicycleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleBicycleNotFoundException(BicycleNotFoundException exception) {
        return exception.getMessage();
    }
}

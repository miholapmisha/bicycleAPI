package com.bravewhool.bicycleAPI.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelBeanConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return  new ModelMapper();
    }

}

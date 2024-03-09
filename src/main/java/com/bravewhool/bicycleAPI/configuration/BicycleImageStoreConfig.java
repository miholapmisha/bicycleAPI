package com.bravewhool.bicycleAPI.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BicycleImageStoreConfig implements WebMvcConfigurer {

    @Value("${filesystem.storage.folder}")
    private String filesystemStorageFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if (!registry.hasMappingForPattern(filesystemStorageFolder)) {
            registry.addResourceHandler("/**")
                    .addResourceLocations("file:///" + filesystemStorageFolder);
        }

    }
}
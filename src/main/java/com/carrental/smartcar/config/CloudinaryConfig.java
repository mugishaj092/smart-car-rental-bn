package com.carrental.smartcar.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhforyx1s",
                "api_key", "577771784241754",
                "api_secret", "nWNOam5NoSZsVq_E4ySFfFZ12fk",
                "secure", true
        ));
    }
}

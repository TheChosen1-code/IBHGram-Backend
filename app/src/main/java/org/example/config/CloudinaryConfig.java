package org.example.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", "dlmvfperf",
                        "api_key", "469135287664584",
                        "api_secret", "2tFOoB13efsHuKKKGmJHqfRb4bE"
                )
        );
    }
}
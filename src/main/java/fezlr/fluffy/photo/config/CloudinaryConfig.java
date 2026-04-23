package fezlr.fluffy.photo.config;

import com.cloudinary.Cloudinary;
import fezlr.fluffy.photo.property.CloudinaryProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class CloudinaryConfig {
    private final CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudinaryProperties.cloudName());
        config.put("api_key", cloudinaryProperties.apiKey());
        config.put("api_secret", cloudinaryProperties.apiSecret());
        return new Cloudinary(config);
    }
}
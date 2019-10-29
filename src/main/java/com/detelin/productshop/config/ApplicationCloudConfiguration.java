package com.detelin.productshop.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class ApplicationCloudConfiguration {
    @Value("${cloudinary.cloud-name}")
    private String cloudAPIName;
    @Value("${cloudinary.api-key}")
    private String cloudAPIKey;
    @Value("${cloudinary.api-secret}")
    private String cloudAPISecret;
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(new HashMap<String,Object>(){{
            put("cloud_name",cloudAPIName);
            put("api_key",cloudAPIKey);
            put("api_secret",cloudAPISecret);
        }});
    }

}

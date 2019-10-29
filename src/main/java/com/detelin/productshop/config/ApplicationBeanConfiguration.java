package com.detelin.productshop.config;

import com.detelin.productshop.domain.models.binding.ProductAddBindingModel;
import com.detelin.productshop.domain.models.service.ProductServiceModel;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class ApplicationBeanConfiguration {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(ProductAddBindingModel.class, ProductServiceModel.class)
                .addMapping(ProductAddBindingModel::getCategories,(dest, value)->dest.setCategories((List<String>) value));
        return new ModelMapper();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

package com.detelin.productshop.domain.models.service;

import com.detelin.productshop.domain.entities.Category;

import java.math.BigDecimal;
import java.util.List;

public class ProductServiceModel extends BaseServiceModel {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private List<String> categories;

    public ProductServiceModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}

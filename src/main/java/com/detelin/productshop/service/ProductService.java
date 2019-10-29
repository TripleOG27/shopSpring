package com.detelin.productshop.service;

import com.detelin.productshop.domain.models.service.ProductServiceModel;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    ProductServiceModel addProduct(ProductServiceModel model);
    List<ProductServiceModel> findAllProducts();
    ProductServiceModel findProductById(String id);

    ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel);

    void deleteProduct(String id);

    List<ProductServiceModel> findAllByCategory(String category);
}

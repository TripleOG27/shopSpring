package com.detelin.productshop.service;

import com.detelin.productshop.domain.entities.Category;
import com.detelin.productshop.domain.entities.Product;
import com.detelin.productshop.domain.models.service.ProductServiceModel;
import com.detelin.productshop.repository.CategoryRepository;
import com.detelin.productshop.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper, CategoryService categoryService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductServiceModel addProduct(ProductServiceModel model) {
        List<Category> categories = categoryRepository.findAll();
        Product product = this.mapper.map(model,Product.class);

        return this.mapper.map(this.productRepository.saveAndFlush(product),ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> findAllProducts() {

        return this.productRepository.findAll().stream().map(p->this.mapper.map(p,ProductServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel findProductById(String id) {
        return this.productRepository.findById(id).map(p->this.mapper.map(p,ProductServiceModel.class)).orElseThrow(()->new IllegalArgumentException("Product doesn't exist"));
    }

    @Override
    public ProductServiceModel editProduct(String id, ProductServiceModel productServiceModel) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with the given id was not found!"));

        productServiceModel.setCategories(
                this.categoryService.findAllCategories()
                        .stream()
                        .filter(c -> productServiceModel.getCategories().contains(c.getId()))
                        .collect(Collectors.toList())
        );

        product.setName(productServiceModel.getName());
        product.setDescription(productServiceModel.getDescription());
        product.setPrice(productServiceModel.getPrice());
        product.setCategories(
                productServiceModel.getCategories()
                        .stream()
                        .map(c -> this.mapper.map(c, Category.class))
                        .collect(Collectors.toList())
        );

        return this.mapper.map(this.productRepository.saveAndFlush(product), ProductServiceModel.class);
    }
    @Override
    public void deleteProduct(String id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product with the given id was not found!"));

        this.productRepository.delete(product);
    }
    @Override
    public List<ProductServiceModel> findAllByCategory(String category) {
        //TODO: OPTIMIZE FILTERING

        return this.productRepository.findAll()
                .stream()
                .filter(product -> product.getCategories().stream().anyMatch(categoryStream -> categoryStream.getName().equals(category)))
                .map(product -> this.mapper.map(product, ProductServiceModel.class))
                .collect(Collectors.toList());
    }

}

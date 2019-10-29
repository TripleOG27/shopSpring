package com.detelin.productshop.service;

import com.detelin.productshop.domain.entities.Category;
import com.detelin.productshop.domain.models.service.CategoryServiceModel;
import com.detelin.productshop.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel) {
        Category category = this.mapper.map(categoryServiceModel,Category.class);
        return this.mapper.map(this.categoryRepository.saveAndFlush(category),CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository.findAll().stream().map(c->this.mapper.map(c,CategoryServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel findCategoryById(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("No such category exists"));
        return this.mapper.map(category,CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(String id, CategoryServiceModel model) {
        Category category = this.categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("No such category exists"));
        category.setName(model.getName());
        return this.mapper.map(this.categoryRepository.saveAndFlush(category),CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel deleteCategory(String id, CategoryServiceModel map) {
        Category category = this.categoryRepository.findById(id).orElseThrow(()->new IllegalArgumentException("No such category exists"));
        this.categoryRepository.delete(category);
        return this.mapper.map(category,CategoryServiceModel.class);
    }
}

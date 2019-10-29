package com.detelin.productshop.service;

import com.detelin.productshop.domain.models.service.CategoryServiceModel;

import java.util.List;

public interface CategoryService {
    CategoryServiceModel addCategory(CategoryServiceModel categoryServiceModel);
    List<CategoryServiceModel> findAllCategories();
    CategoryServiceModel findCategoryById(String id);
    CategoryServiceModel editCategory(String id,CategoryServiceModel model);

    CategoryServiceModel deleteCategory(String id, CategoryServiceModel map);
}

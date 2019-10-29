package com.detelin.productshop.web.controllers;

import com.detelin.productshop.domain.models.binding.CategoryAddBindingModel;
import com.detelin.productshop.domain.models.service.CategoryServiceModel;
import com.detelin.productshop.domain.models.view.CategoryAllViewModel;
import com.detelin.productshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper mapper;
    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategory(){
        return super.view("category/add-category");
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addCategoryConfirm(@ModelAttribute CategoryAddBindingModel model){
        this.categoryService.addCategory(this.mapper.map(model, CategoryServiceModel.class));
        return super.redirect("/categories/all");
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allCategories(ModelAndView modelAndView){
        modelAndView.addObject("categories",this.categoryService.findAllCategories().stream()
                .map(c->this.mapper.map(c, CategoryAllViewModel.class)).collect(Collectors.toList()));
        return super.view("category/all-categories",modelAndView);

    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategory(@PathVariable String id,ModelAndView modelAndView){
        modelAndView.addObject("model",this.mapper.map(this.categoryService.findCategoryById(id),CategoryAllViewModel.class));
        return super.view("category/edit-category",modelAndView);
    }
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editCategoryConfirm(@PathVariable String id,@ModelAttribute CategoryAddBindingModel model){
        this.categoryService.editCategory(id,this.mapper.map(model,CategoryServiceModel.class));
        return super.redirect("/categories/all");
    }
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategory(@PathVariable String id,ModelAndView modelAndView){
        modelAndView.addObject("model",this.mapper.map(this.categoryService.findCategoryById(id),CategoryAllViewModel.class));
        return super.view("category/delete-category",modelAndView);
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteCategoryConfirm(@PathVariable String id,@ModelAttribute CategoryAddBindingModel model){
        this.categoryService.deleteCategory(id,this.mapper.map(model,CategoryServiceModel.class));
        return super.redirect("/categories/all");
    }
    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<CategoryAllViewModel> fetchCategories(){
        return this.categoryService.findAllCategories().stream().map(c->this.mapper.map(c,CategoryAllViewModel.class)).collect(Collectors.toList());
    }
}

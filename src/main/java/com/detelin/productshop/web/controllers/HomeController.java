package com.detelin.productshop.web.controllers;

import com.detelin.productshop.domain.models.view.CategoryAllViewModel;
import com.detelin.productshop.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {
    private final CategoryService categoryService;
    private final ModelMapper mapper;

    public HomeController(CategoryService categoryService, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.mapper = mapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index(){
        return super.view("index");
    }
    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
//    @PageTitle("Home")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.addObject("categories", this.categoryService.findAllCategories().stream().map(category -> this.mapper.map(category, CategoryAllViewModel.class)).collect(Collectors.toList()));

        return super.view("home", modelAndView);
    }
}

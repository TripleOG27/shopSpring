package com.detelin.productshop.web.controllers;

import com.detelin.productshop.domain.models.binding.ProductAddBindingModel;
import com.detelin.productshop.domain.models.service.CategoryServiceModel;
import com.detelin.productshop.domain.models.service.ProductServiceModel;
import com.detelin.productshop.domain.models.view.ProductAllViewModel;
import com.detelin.productshop.domain.models.view.ProductDetailsViewModel;
import com.detelin.productshop.service.CategoryService;
import com.detelin.productshop.service.CloudinaryService;
import com.detelin.productshop.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {
    private final ProductService productService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper mapper;
    private final CategoryService categoryService;
    @Autowired
    public ProductController(ProductService productService, CloudinaryService cloudinaryService, ModelMapper mapper, CategoryService categoryService) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
        this.mapper = mapper;
        this.categoryService = categoryService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProduct(){
        return super.view("product/add-product");
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addProductConfirm(@ModelAttribute ProductAddBindingModel model) throws IOException {
        ProductServiceModel productServiceModel = this.mapper.map(model,ProductServiceModel.class);
//        productServiceModel.setCategories(this.categoryService.findAllCategories().stream().filter(c->model.getCategories().contains(c.getId())).collect(Collectors.toList()));
        productServiceModel.setImageUrl(this.cloudinaryService.uploadImage(model.getImage()));
        this.productService.addProduct(productServiceModel);
        return super.redirect("/products/all");
    }
    @GetMapping("all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView allProducts(ModelAndView modelAndView){
        modelAndView.addObject("products",this.productService.findAllProducts().stream()
                .map(p->this.mapper.map(p, ProductAllViewModel.class)).collect(Collectors.toList()));
        return super.view("product/all-products",modelAndView);

    }
    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView productDetails(@PathVariable String id,ModelAndView modelAndView){
        modelAndView.addObject("product",this.mapper.map(this.productService.findProductById(id),ProductDetailsViewModel.class));
        return super.view("product/details",modelAndView);
    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProduct(@PathVariable String id, ModelAndView modelAndView){
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductAddBindingModel model = this.mapper.map(productServiceModel,ProductAddBindingModel.class);
        model.setCategories(productServiceModel.getCategories().stream().map(CategoryServiceModel::getName).collect(Collectors.toList()));
        modelAndView.addObject("product",model);
        modelAndView.addObject("productId",id);
        return super.view("product/edit-product",modelAndView);
    }
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editProductConfirm(@PathVariable String id, @ModelAttribute ProductAddBindingModel model) {
        this.productService.editProduct(id, this.mapper.map(model, ProductServiceModel.class));

        return super.redirect("/products/details/" + id);
    }
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
//    @PageTitle("Delete Product")
    public ModelAndView deleteProduct(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductAddBindingModel model = this.mapper.map(productServiceModel, ProductAddBindingModel.class);
        model.setCategories(productServiceModel.getCategories().stream().map(CategoryServiceModel::getName).collect(Collectors.toList()));

        modelAndView.addObject("product", model);
        modelAndView.addObject("productId", id);

        return super.view("product/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteProductConfirm(@PathVariable String id) {
        this.productService.deleteProduct(id);

        return super.redirect("/products/all");
    }
}

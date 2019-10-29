package com.detelin.productshop.web.controllers;

import com.detelin.productshop.domain.models.binding.UserEditBindingModel;
import com.detelin.productshop.domain.models.binding.UserRegisterBindingModel;
import com.detelin.productshop.domain.models.service.RoleServiceModel;
import com.detelin.productshop.domain.models.service.UserServiceModel;
import com.detelin.productshop.domain.models.view.UserAllViewModel;
import com.detelin.productshop.domain.models.view.UserProfileViewModel;
import com.detelin.productshop.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.PushBuilder;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {
    private final UserService userService;
    private final ModelMapper mapper;

    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerUser(){
        return super.view("register");
    }
    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView confirmRegister(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel){
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            return super.view("register");
        }
        this.userService.registerUser(this.mapper.map(userRegisterBindingModel, UserServiceModel.class));
        return super.redirect("/login");

    }
    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(){
        return super.view("login");
    }
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal,ModelAndView modelAndView){
        modelAndView.addObject("model",this.mapper.map(this.userService.findUserByUsername(principal.getName()), UserProfileViewModel.class));
        return super.view("profile",modelAndView);
    }
    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal,ModelAndView modelAndView){
        modelAndView.addObject("model",this.mapper.map(this.userService.findUserByUsername(principal.getName()), UserProfileViewModel.class));
        return super.view("edit-profile",modelAndView);
    }
    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute UserEditBindingModel model) throws IllegalAccessException {
        if(!model.getPassword().equals(model.getConfirmPassword())){
            return super.view("edit-profile");
        }
        this.userService.editUserProfile(this.mapper.map(model,UserServiceModel.class),model.getOldPassword());
        return super.redirect("/users/profile");
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView){
        List<UserAllViewModel> users = this.userService.findAllUsers().stream()
                .map(u->{
                    UserAllViewModel user = this.mapper.map(u,UserAllViewModel.class);
                    user.setAuthorities(u.getAuthorities().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toSet()));
                    return user;
                }).collect(Collectors.toList());
        modelAndView.addObject("users",users);
    return super.view("all-users",modelAndView);
    }
    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id){
        this.userService.setUserRole(id,"user");
        return super.redirect("/users/all");
    }
    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id){
        this.userService.setUserRole(id,"admin");
        return super.redirect("/users/all");
    }
    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id){
        this.userService.setUserRole(id,"moderator");
        return super.redirect("/users/all");
    }
}

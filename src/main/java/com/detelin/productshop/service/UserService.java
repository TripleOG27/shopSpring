package com.detelin.productshop.service;

import com.detelin.productshop.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserServiceModel registerUser(UserServiceModel userServiceModel);
    UserServiceModel findUserByUsername(String username);
    UserServiceModel editUserProfile(UserServiceModel model,String oldPassword) throws IllegalAccessException;
    List<UserServiceModel> findAllUsers();
    void setUserRole(String id,String role);
}

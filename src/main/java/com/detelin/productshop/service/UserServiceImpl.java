package com.detelin.productshop.service;

import com.detelin.productshop.domain.entities.User;
import com.detelin.productshop.domain.models.service.UserServiceModel;
import com.detelin.productshop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.mapper = mapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        this.roleService.seedRolesInDB();
        if(this.userRepository.count()==0){
            userServiceModel.setAuthorities(this.roleService.findAllRoles());
        }else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
        }

        User user = this.mapper.map(userServiceModel,User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));
        return this.mapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("Username not found")
        );
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {

        return this.userRepository.findByUsername(username).map(u->this.mapper.map(u,UserServiceModel.class))
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel model, String oldPassword) throws IllegalAccessException {
        User user = this.userRepository.findByUsername(model.getUsername()).orElseThrow(()->new UsernameNotFoundException("User not found"));
        if(!this.bCryptPasswordEncoder.matches(oldPassword,user.getPassword())){
            throw new IllegalAccessException("Incorrect Password");
        }
        user.setPassword(!"".equals(model.getPassword())?this.bCryptPasswordEncoder.encode(model.getPassword()):this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEmail(model.getEmail());
        return this.mapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAllUsers() {
        return this.userRepository.findAll().stream().map(u->this.mapper.map(u,UserServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public void setUserRole(String id, String role) {
        User user = this.userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("Username not found"));
        UserServiceModel userServiceModel = this.mapper.map(user,UserServiceModel.class);
        userServiceModel.getAuthorities().clear();
        switch (role){
            case "user":userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER")); break;
            case "moderator":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
                userServiceModel.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }
        this.userRepository.saveAndFlush(this.mapper.map(userServiceModel,User.class));
    }
}

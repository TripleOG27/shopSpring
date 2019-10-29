package com.detelin.productshop.service;

import com.detelin.productshop.domain.entities.Role;
import com.detelin.productshop.domain.models.service.RoleServiceModel;
import com.detelin.productshop.domain.models.service.UserServiceModel;
import com.detelin.productshop.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper mapper) {
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public void seedRolesInDB() {
        if(this.roleRepository.count()==0){
            this.roleRepository.saveAndFlush(new Role("ROLE_USER"));
            this.roleRepository.saveAndFlush(new Role("ROLE_MODERATOR"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ADMIN"));
            this.roleRepository.saveAndFlush(new Role("ROLE_ROOT"));
        }
    }

//    @Override
//    public void assignUserRoles(UserServiceModel userServiceModel,long numberOfUsers) {
//        if(numberOfUsers==0){
//            userServiceModel.setAuthorities(this.roleRepository.findAll()
//                    .stream()
//                    .map(r->this.mapper.map(r, RoleServiceModel.class))
//            .collect(Collectors.toSet()));
//        }
//    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository.findAll().stream()
                .map(r->this.mapper.map(r,RoleServiceModel.class)).collect(Collectors.toSet());
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return this.mapper.map(this.roleRepository.findByAuthority(authority),RoleServiceModel.class);
    }
}

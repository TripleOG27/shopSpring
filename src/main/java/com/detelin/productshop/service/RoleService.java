package com.detelin.productshop.service;

import com.detelin.productshop.domain.models.service.RoleServiceModel;
import com.detelin.productshop.domain.models.service.UserServiceModel;

import java.util.Set;

public interface RoleService {
    void seedRolesInDB();
//    void assignUserRoles(UserServiceModel userServiceModel,long numberOfUsers);
    Set<RoleServiceModel> findAllRoles();
    RoleServiceModel findByAuthority(String authority);
}

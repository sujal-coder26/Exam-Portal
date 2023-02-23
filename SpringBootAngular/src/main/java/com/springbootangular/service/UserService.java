package com.springbootangular.service;

import com.springbootangular.entities.User;
import com.springbootangular.entities.UserRole;

import java.util.Set;

public interface UserService {

     User createUser(User user, Set<UserRole> userRoles) throws Exception;

     User getUser(String username);

     void deleteUser(Long userId);
}

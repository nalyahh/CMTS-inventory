package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<Production> getAllProductions(Long id);

    User createUser(CreateUserRequest createUserRequest);

    void deleteUser(Long id);

}
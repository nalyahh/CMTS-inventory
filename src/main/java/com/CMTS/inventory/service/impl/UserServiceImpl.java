package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.repository.UserRepository;
import com.CMTS.inventory.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductionRepository productionRepository;

    public UserServiceImpl(UserRepository userRepository, ProductionRepository productionRepository) {
        this.userRepository = userRepository;
        this.productionRepository = productionRepository;
    }

    public List<User> getAllUsers() {
            return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User saveUser(CreateUserRequest request) {
            User user = new User();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPassword(request.password());
            user.setRole(request.role());
        if (request.productionId() != null) {
            Production production = productionRepository.findById(request.productionId())
                    .orElseThrow(() -> new RuntimeException("Production not found"));
            user.setProduction(production);
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}

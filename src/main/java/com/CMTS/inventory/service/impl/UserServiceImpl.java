package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.repository.UserRepository;
import com.CMTS.inventory.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductionRepository productionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ProductionRepository productionRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productionRepository = productionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
            return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));    }

    @Override
    public User createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));        user.setRole(request.role());
        if (request.productionIds() != null && !request.productionIds().isEmpty()) {
            for (Long productionId : request.productionIds()) {
                Production production = productionRepository.findById(productionId)
                        .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + productionId + " not found"));
                user.getProductions().add(production);
            }
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}

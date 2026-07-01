package com.CMTS.inventory.controller;

import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.dto.CreateUserRequestDto;
import com.CMTS.inventory.domain.dto.ProductionDto;
import com.CMTS.inventory.domain.dto.UserDto;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.mapper.ProductionMapper;
import com.CMTS.inventory.mapper.UserMapper;
import com.CMTS.inventory.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProductionMapper productionMapper;


    public UserController(UserService userService, UserMapper userMapper, ProductionMapper productionMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.productionMapper = productionMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser (@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        CreateUserRequest createUserRequest = userMapper.fromDTO(createUserRequestDto);
        User user = userService.createUser(createUserRequest);
        UserDto createdUserRequestDto = userMapper.toDto(user);
        return new ResponseEntity<>(createdUserRequestDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{id}/productions")
    public ResponseEntity<List<ProductionDto>> getAllProductions(@PathVariable Long id) {
        List<Production> productions = userService.getAllProductions(id);
        List<ProductionDto> productionDtos = productions.stream()
                .map(productionMapper::toDto)
                .toList();
        return ResponseEntity.ok(productionDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}

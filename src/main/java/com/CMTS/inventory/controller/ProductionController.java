package com.CMTS.inventory.controller;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.dto.CreateProductionRequestDto;
import com.CMTS.inventory.domain.dto.ProductionDto;
import com.CMTS.inventory.domain.dto.UserDto;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.mapper.ProductionMapper;
import com.CMTS.inventory.mapper.UserMapper;
import com.CMTS.inventory.service.ProductionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productions")
public class ProductionController {

    private final ProductionService productionService;
    private final ProductionMapper productionMapper;
    private final UserMapper userMapper;


    public ProductionController(ProductionService productionService, ProductionMapper productionMapper, UserMapper userMapper) {
        this.productionService = productionService;
        this.productionMapper = productionMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductionDto>> getAllProductions() {
        List<Production> productions = productionService.getAllProductions();
        List<ProductionDto> productionDtos = productions.stream()
                .map(productionMapper::toDto)
                .toList();
        return ResponseEntity.ok(productionDtos);
    }

    @PostMapping
    public ResponseEntity<ProductionDto> createProduction (@Valid @RequestBody CreateProductionRequestDto createProductionRequestDto) {
        CreateProductionRequest createProductionRequest = productionMapper.fromDTO(createProductionRequestDto);
        Production production = productionService.createProduction(createProductionRequest);
        ProductionDto createdProductionRequestDto = productionMapper.toDto(production);
        return new ResponseEntity<>(createdProductionRequestDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionDto> getProductionById(@PathVariable Long id) {
        Production production = productionService.getProductionById(id);
        ProductionDto productionDto = productionMapper.toDto(production);
        return ResponseEntity.ok(productionDto);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductionDto> getProductionByName(@PathVariable String name) {
        Production production = productionService.getProductionByName(name);
        ProductionDto productionDto = productionMapper.toDto(production);
        return ResponseEntity.ok(productionDto);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ProductionDto> archiveProduction(@PathVariable Long id) {
        Production production = productionService.archiveProduction(id);
        ProductionDto productionDto = productionMapper.toDto(production);
        return ResponseEntity.ok(productionDto);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserDto>> getAllUsers(@PathVariable Long id) {
        List<User> users = productionService.getAllUsers(id);
        List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduction(@PathVariable Long id) {
        productionService.deleteProduction(id);
        return ResponseEntity.noContent().build();
    }

}

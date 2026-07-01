package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;

import java.util.List;

public interface ProductionService {

    List<Production> getAllProductions();

    Production getProductionById(Long id);

    Production getProductionByName(String name);

    List<User> getAllUsers (Long id);

    Production createProduction(CreateProductionRequest createProductionRequest);

    Production archiveProduction(Long id);

    void deleteProduction(Long id);
}
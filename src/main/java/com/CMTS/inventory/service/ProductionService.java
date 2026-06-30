package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.entity.Production;

import java.util.List;

public interface ProductionService {

    List<Production> getAllProductions();

    Production getProductionById(Long id);

    Production getProductionByName(String name);

    Production saveProduction(CreateProductionRequest request);

    void deleteProduction(Long id);
}
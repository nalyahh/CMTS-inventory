package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.service.ProductionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionServiceImpl implements ProductionService {

    private final ProductionRepository productionRepository;

    public ProductionServiceImpl(ProductionRepository productionRepository) {
        this.productionRepository = productionRepository;
    }

    public List<Production> getAllProductions() {
        return productionRepository.findAll();
    }

    public Production getProductionById(Long id) {
        return productionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + id + " not found"));    }

    public Production getProductionByName(String name) {
        return productionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Production with name " + name + " not found"));    }

    public Production createProduction(CreateProductionRequest request) {
        Production production = new Production();
        production.setName(request.name());
        production.setStartDate(request.startDate());
        production.setEndDate(request.endDate());
        return productionRepository.save(production);
    }

    public void deleteProduction(Long id) {
        productionRepository.deleteById(id);
    }
}

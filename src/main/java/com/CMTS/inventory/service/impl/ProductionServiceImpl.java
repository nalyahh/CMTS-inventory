package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.exception.ProductionNotArchivableException;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.repository.CheckoutRepository;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.repository.UserRepository;
import com.CMTS.inventory.service.ProductionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionServiceImpl implements ProductionService {

    private final ProductionRepository productionRepository;
    private final CheckoutRepository checkoutRepository;
    private final UserRepository userRepository;

    public ProductionServiceImpl(ProductionRepository productionRepository, CheckoutRepository checkoutRepository, UserRepository userRepository) {
        this.productionRepository = productionRepository;
        this.checkoutRepository = checkoutRepository;
        this.userRepository = userRepository;
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

    @Override
    public List<User> getAllUsers(Long id) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + id + " not found"));
        return userRepository.findByProductions(production);
    }

    public Production createProduction(CreateProductionRequest request) {
        Production production = new Production();
        production.setName(request.name());
        production.setStartDate(request.startDate());
        production.setEndDate(request.endDate());
        return productionRepository.save(production);
    }

    @Override
    public Production archiveProduction(Long id) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + id + " not found"));

        List<Checkout> activeCheckouts = checkoutRepository.findByProductionAndReturnedAtIsNull(production);

        if (!activeCheckouts.isEmpty())
            throw new ProductionNotArchivableException("Production " + production.getName() + " has active checkouts and cannot be archived");

        production.setArchived(true);
        return productionRepository.save(production);
    }

    public void deleteProduction(Long id) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + id + " not found"));

        List<Checkout> activeCheckouts = checkoutRepository.findByProductionAndReturnedAtIsNull(production);

        if (!activeCheckouts.isEmpty())
            throw new ProductionNotArchivableException("Production " + production.getName() + " has active checkouts and cannot be deleted");

        productionRepository.deleteById(id);
    }
}

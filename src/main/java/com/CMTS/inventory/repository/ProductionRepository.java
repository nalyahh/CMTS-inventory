package com.CMTS.inventory.repository;

import com.CMTS.inventory.domain.entity.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

    Optional<Production> findByName(String name);

    List<Production> getProductionById(Long id);
}

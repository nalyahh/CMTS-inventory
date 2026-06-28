package com.CMTS.inventory.repository;

import com.CMTS.inventory.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {

    List<Production> findByName(String name);
}

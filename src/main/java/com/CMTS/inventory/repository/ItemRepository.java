package com.CMTS.inventory.repository;

import com.CMTS.inventory.model.Item;
import com.CMTS.inventory.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByStatus(Item.Status status);

    List<Item> findByCategory(Item.Categories Category);

    List<Item> findByProduction (Production production);

    List<Item> findByLocation (String location);
}

package com.CMTS.inventory.repository;

import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    List<Checkout> findByUser(User user);

    List<Checkout> findByReturnedAtIsNull();

    List<Checkout> findByItemAndReturnedAtIsNull(Item item);

    List<Checkout> findByProduction(Production production);

    List<Checkout> findByProductionAndReturnedAtIsNull(Production production);

}

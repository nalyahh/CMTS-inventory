package com.CMTS.inventory.repository;

import com.CMTS.inventory.model.Checkout;
import com.CMTS.inventory.model.Production;
import com.CMTS.inventory.model.User;
import com.CMTS.inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    List<Checkout> findByUser(User user);

    List<Checkout> findByReturnedAtIsNull();

    List<Checkout> findByItemAndReturnedAtIsNull(Item item);

    List<Checkout> findByProduction(Production production);

}

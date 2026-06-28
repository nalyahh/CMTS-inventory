package com.CMTS.inventory.service;

import com.CMTS.inventory.model.Checkout;
import com.CMTS.inventory.model.Item;
import com.CMTS.inventory.model.Production;
import com.CMTS.inventory.model.User;
import com.CMTS.inventory.repository.CheckoutRepository;
import com.CMTS.inventory.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.CMTS.inventory.model.Item.Status.AVAILABLE;

@Service
public class CheckoutService {

    private final ItemRepository itemRepository;
    private final CheckoutRepository checkoutRepository;

    public CheckoutService(ItemRepository itemRepository, CheckoutRepository checkoutRepository) {
        this.itemRepository = itemRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public Checkout checkoutItem(Long itemId, User user, Production production, LocalDate dueDate) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getStatus() != Item.Status.AVAILABLE)
            throw new RuntimeException("Item is not available");


        Checkout checkout = new Checkout();
        checkout.setItem(item);
        checkout.setUser(user);
        checkout.setProduction(production);
        checkout.setDueDate(dueDate);
        checkout.setCheckedOutAt(LocalDateTime.now());

        item.setStatus(Item.Status.CHECKED_OUT);

        itemRepository.save(item);
        return checkoutRepository.save(checkout);
    }

    public Checkout checkInItem(Long itemID) {
        Item item = itemRepository.findById(itemID).orElseThrow(() -> new RuntimeException("Item not found"));
        List<Checkout> activeCheckouts = checkoutRepository.findByItemAndReturnedAtIsNull(item);

        if(activeCheckouts.isEmpty())
            throw new RuntimeException("No active checkout found");
        Checkout checkout = activeCheckouts.getFirst();

        checkout.setReturnedAt(LocalDateTime.now());
        checkout.getItem().setStatus(AVAILABLE);

        itemRepository.save(item);
        return checkoutRepository.save(checkout);
    }

}

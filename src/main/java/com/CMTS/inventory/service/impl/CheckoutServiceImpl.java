package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.exception.ItemNotAvailableException;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.repository.CheckoutRepository;
import com.CMTS.inventory.repository.ItemRepository;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.repository.UserRepository;
import com.CMTS.inventory.service.CheckoutService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import static com.CMTS.inventory.domain.entity.Item.Status.AVAILABLE;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private final ItemRepository itemRepository;
    private final CheckoutRepository checkoutRepository;
    private final UserRepository userRepository;
    private final ProductionRepository productionRepository;
    private static final int CHECKOUT_BUFFER_DAYS = 4;

    public CheckoutServiceImpl(ItemRepository itemRepository, CheckoutRepository checkoutRepository, UserRepository userRepository, ProductionRepository productionRepository) {
        this.itemRepository = itemRepository;
        this.checkoutRepository = checkoutRepository;
        this.userRepository = userRepository;
        this.productionRepository = productionRepository;
    }

    public Checkout checkoutItem(CheckoutRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + request.userId() + " not found"));

        Production production = productionRepository.findById(request.productionId())
                .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + request.productionId() + " not found"));

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + request.itemId() + " not found"));

        if (item.getStatus() != Item.Status.AVAILABLE)
            throw new ItemNotAvailableException("Item with ID " + request.itemId() + " is not available");

        Checkout checkout = new Checkout();
        checkout.setItem(item);
        checkout.setUser(user);
        checkout.setProduction(production);
        checkout.setDueDate(production.getEndDate().plusDays(CHECKOUT_BUFFER_DAYS));        checkout.setCheckedOutAt(LocalDateTime.now());
        item.setStatus(Item.Status.CHECKED_OUT);

        itemRepository.save(item);
        return checkoutRepository.save(checkout);
    }

    public Checkout checkInItem(Long itemID) {
        Item item = itemRepository.findById(itemID).orElseThrow(() -> new ResourceNotFoundException("Item with ID " + itemID + " not found"));
        List<Checkout> activeCheckouts = checkoutRepository.findByItemAndReturnedAtIsNull(item);

        if(activeCheckouts.isEmpty())
            throw new ResourceNotFoundException("No active checkout found for item " + itemID);
        Checkout checkout = activeCheckouts.getFirst();

        checkout.setReturnedAt(LocalDateTime.now());
        checkout.getItem().setStatus(AVAILABLE);

        itemRepository.save(item);
        return checkoutRepository.save(checkout);
    }

}

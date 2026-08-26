package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.exception.ArchivedProductionException;
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
        if (production.isArchived())
            throw new ArchivedProductionException("Production " + production.getName() + " is archived and cannot be checked out against");

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + request.itemId() + " not found"));
        if (item.isArchived())
            throw new ItemNotAvailableException(item.getName() + " has been retired and cannot be checked out");

        int activeCheckouts = checkoutRepository.countByItemAndReturnedAtIsNull(item);
        if (activeCheckouts >= item.getQuantity())
            throw new ItemNotAvailableException(item.getName() + " is not available! (0 of " + item.getQuantity() + " left)");
        Checkout checkout = new Checkout();
        checkout.setItem(item);
        checkout.setUser(user);
        checkout.setProduction(production);
        checkout.setDueDate(production.getEndDate().plusDays(CHECKOUT_BUFFER_DAYS));
        checkout.setCheckedOutAt(LocalDateTime.now());
        item.setStatus(activeCheckouts + 1 >= item.getQuantity() ? Item.Status.CHECKED_OUT : Item.Status.AVAILABLE);

        itemRepository.save(item);
        return checkoutRepository.save(checkout);
    }

    public Checkout checkInItem(Long checkoutId) {
        Checkout checkout = checkoutRepository.findById(checkoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkout with ID " + checkoutId + " not found"));

        if (checkout.getReturnedAt() != null)
            throw new ItemNotAvailableException(checkout.getItem().getName() + " has already been returned");

        checkout.setReturnedAt(LocalDateTime.now());

        Item item = checkout.getItem();
        item.setStatus(AVAILABLE);
        itemRepository.save(item);

        return checkoutRepository.save(checkout);
    }

    @Override
    public List<Checkout> getAllActiveCheckouts() {
        return checkoutRepository.findByReturnedAtIsNull();
    }

    @Override
    public List<Checkout> getMyActiveCheckouts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        return checkoutRepository.findByUserAndReturnedAtIsNull(user);
    }
}

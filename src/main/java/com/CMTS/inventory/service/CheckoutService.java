package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.entity.Checkout;

public interface CheckoutService {

    Checkout checkoutItem(CheckoutRequest request);

    Checkout checkInItem(Long itemId);
}
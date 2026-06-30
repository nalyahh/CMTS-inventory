package com.CMTS.inventory.domain;

import com.CMTS.inventory.domain.entity.Item;

public record UpdateItemRequest(
        String name,
        String location,
        Item.Categories category,
        int quantity,
        String notes,
        String photoURL,
        Long productionId) {
}

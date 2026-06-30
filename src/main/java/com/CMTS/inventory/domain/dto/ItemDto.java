package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.Item;

public record ItemDto(
        Long id,
        String name,
        String location,
        Item.Categories category,
        Item.Status status,
        int quantity,
        String notes,
        String photoURL,
        Long productionId) {
}

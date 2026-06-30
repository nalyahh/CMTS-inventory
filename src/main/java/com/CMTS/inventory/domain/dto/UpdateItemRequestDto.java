package com.CMTS.inventory.domain.dto;

import com.CMTS.inventory.domain.entity.Item;

public record UpdateItemRequestDto(
        String name,
        String location,
        Item.Categories category,
        int quantity,
        String notes,
        String photoURL,
        Long productionId
) {
}

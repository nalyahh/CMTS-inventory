package com.CMTS.inventory.mapper;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.dto.CreateItemRequestDto;
import com.CMTS.inventory.domain.dto.ItemDto;
import com.CMTS.inventory.domain.dto.UpdateItemRequestDto;
import com.CMTS.inventory.domain.entity.Item;

public interface ItemMapper {

    CreateItemRequest fromDTO(CreateItemRequestDto dto);
    UpdateItemRequest fromUpdateDTO(UpdateItemRequestDto dto);
    ItemDto toDto(Item item);
}



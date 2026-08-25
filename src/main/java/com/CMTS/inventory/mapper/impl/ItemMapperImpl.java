package com.CMTS.inventory.mapper.impl;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.dto.CreateItemRequestDto;
import com.CMTS.inventory.domain.dto.ItemDto;
import com.CMTS.inventory.domain.dto.UpdateItemRequestDto;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.mapper.ItemMapper;
import com.CMTS.inventory.repository.CheckoutRepository;
import com.CMTS.inventory.repository.ItemPhotoRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemMapperImpl implements ItemMapper {

    private final CheckoutRepository checkoutRepository;
    private final ItemPhotoRepository itemPhotoRepository;

    public ItemMapperImpl(CheckoutRepository checkoutRepository, ItemPhotoRepository itemPhotoRepository) {
        this.checkoutRepository = checkoutRepository;
        this.itemPhotoRepository = itemPhotoRepository;
    }

    @Override
    public CreateItemRequest fromDTO (CreateItemRequestDto dto) {
        return new CreateItemRequest(
                dto.name(),
                dto.location(),
                dto.category(),
                dto.quantity(),
                dto.notes(),
                dto.photoURL(),
                dto.productionId()
        );
    }

    @Override
    public UpdateItemRequest fromUpdateDTO(UpdateItemRequestDto dto) {
        return new UpdateItemRequest(
                dto.name(),
                dto.location(),
                dto.category(),
                dto.quantity(),
                dto.notes(),
                dto.photoURL(),
                dto.productionId()
        );
    }

    @Override
    public ItemDto toDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getLocation(),
                item.getCategory(),
                item.getStatus(),
                item.getQuantity(),
                item.getQuantity() - checkoutRepository.countByItemAndReturnedAtIsNull(item),
                item.getNotes(),
                itemPhotoRepository.existsById(item.getId()) ? "/items/" + item.getId() + "/photo" : null,
                item.getProduction() != null ? item.getProduction().getId() : null
        );
    }
}

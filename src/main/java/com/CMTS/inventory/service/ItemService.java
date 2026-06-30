package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.entity.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();

    Item getItemById(Long id);

    List<Item> getItemByStatus(Item.Status status);

    Item createItem(CreateItemRequest request);

    Item updateItem(Long id, UpdateItemRequest request);

    Item saveItem(Item item);

    void deleteItem(Long id);
}
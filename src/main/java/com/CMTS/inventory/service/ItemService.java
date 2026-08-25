package com.CMTS.inventory.service;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.domain.entity.ItemPhoto;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();

    Item getItemById(Long id);

    List<Item> getItemByStatus(Item.Status status);

    void savePhoto(Long itemId, byte[] data, String contentType);

    ItemPhoto getPhoto(Long itemId);

    Item createItem(CreateItemRequest request);

    Item updateItem(Long id, UpdateItemRequest request);

    void deleteItem(Long id);

    List<Item> getAllItemsIncludingArchived();

    Item setArchived(Long id, boolean archived);
}
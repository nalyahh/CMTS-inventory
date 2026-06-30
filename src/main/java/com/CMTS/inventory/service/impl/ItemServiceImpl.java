package com.CMTS.inventory.service.impl;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.exception.ResourceNotFoundException;
import com.CMTS.inventory.repository.ItemRepository;
import com.CMTS.inventory.repository.ProductionRepository;
import com.CMTS.inventory.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ProductionRepository productionRepository;


    public ItemServiceImpl(ItemRepository itemRepository, ProductionRepository productionRepository) {
        this.itemRepository = itemRepository;
        this.productionRepository = productionRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + id + " not found"));
    }

    public List<Item> getItemByStatus(Item.Status status) {
        return itemRepository.findByStatus(status);
    }

    public Item createItem(CreateItemRequest request) {

        Item item = new Item();
        item.setName(request.name());
        item.setLocation(request.location());
        item.setCategory(request.category());
        item.setQuantity(request.quantity());
        if (request.productionId() != null) {
            Production production = productionRepository.findById(request.productionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + request.productionId() + " not found"));            item.setProduction(production);
        }
        item.setNotes(request.notes());
        item.setPhotoURL(request.photoURL());
        item.setStatus(Item.Status.AVAILABLE);

        return itemRepository.save(item);
    }

    public Item updateItem(Long id, UpdateItemRequest request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with ID " + id + " not found"));
        item.setName(request.name());
        item.setLocation(request.location());
        item.setCategory(request.category());
        item.setQuantity(request.quantity());
        if (request.productionId() != null) {
            Production production = productionRepository.findById(request.productionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Production with ID " + request.productionId() + " not found"));
            item.setProduction(production);
        }
        item.setNotes(request.notes());
        item.setPhotoURL(request.photoURL());

        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}

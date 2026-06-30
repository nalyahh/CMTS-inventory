package com.CMTS.inventory.controller;

import com.CMTS.inventory.domain.CreateItemRequest;
import com.CMTS.inventory.domain.UpdateItemRequest;
import com.CMTS.inventory.domain.dto.CreateItemRequestDto;
import com.CMTS.inventory.domain.dto.ItemDto;
import com.CMTS.inventory.domain.dto.UpdateItemRequestDto;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.mapper.ItemMapper;
import com.CMTS.inventory.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        List<ItemDto> itemDtos = items.stream()
                .map(itemMapper::toDto)
                .toList();
        return ResponseEntity.ok(itemDtos);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem (@Valid @RequestBody CreateItemRequestDto createItemRequestDto) {
        CreateItemRequest createItemRequest = itemMapper.fromDTO(createItemRequestDto);
        Item item = itemService.createItem(createItemRequest);
        ItemDto createdItemRequestDto = itemMapper.toDto(item);
        return new ResponseEntity<>(createdItemRequestDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        ItemDto itemDto = itemMapper.toDto(item);
        return ResponseEntity.ok(itemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @Valid @RequestBody UpdateItemRequestDto updateItemRequestDto) {
        UpdateItemRequest updateItemRequest = itemMapper.fromUpdateDTO(updateItemRequestDto);
        Item item = itemService.updateItem(id, updateItemRequest);
        ItemDto updatedItemDto = itemMapper.toDto(item);
        return ResponseEntity.ok(updatedItemDto);
    }

}
package com.CMTS.inventory.controller;


import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.dto.CheckoutDto;
import com.CMTS.inventory.domain.dto.CheckoutRequestDto;
import com.CMTS.inventory.domain.dto.ItemDto;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.domain.entity.Item;
import com.CMTS.inventory.mapper.CheckoutMapper;
import com.CMTS.inventory.mapper.ItemMapper;
import com.CMTS.inventory.service.CheckoutService;
import com.CMTS.inventory.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkouts")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CheckoutMapper checkoutMapper;
    private final ItemMapper itemMapper;
    private final ItemService itemService;

    public CheckoutController(CheckoutService checkoutService, CheckoutMapper checkoutMapper, ItemMapper itemMapper, ItemService itemService) {
        this.checkoutService = checkoutService;
        this.checkoutMapper = checkoutMapper;
        this.itemMapper = itemMapper;
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<CheckoutDto> checkoutItem(@Valid @RequestBody CheckoutRequestDto checkoutRequestDto) {
        CheckoutRequest checkoutRequest = checkoutMapper.fromDTO(checkoutRequestDto);
        Checkout checkout = checkoutService.checkoutItem(checkoutRequest);
        CheckoutDto createdCheckoutRequestDto = checkoutMapper.toDto(checkout);
        return new ResponseEntity<>(createdCheckoutRequestDto, HttpStatus.CREATED);
    }

    @PutMapping("/items/{itemId}/checkin")
    public ResponseEntity<CheckoutDto> checkInItem(@PathVariable Long itemId) {
        Checkout checkout = checkoutService.checkInItem(itemId);
        CheckoutDto checkoutDto = checkoutMapper.toDto(checkout);
        return ResponseEntity.ok(checkoutDto);
    }

    @GetMapping
    public ResponseEntity<List<CheckoutDto>> getAllActiveCheckouts() {
        List<Checkout> checkouts = checkoutService.getAllActiveCheckouts();
        List<CheckoutDto> checkoutDtos = checkouts.stream()
                .map(checkoutMapper::toDto)
                .toList();
        return ResponseEntity.ok(checkoutDtos);
    }

    @GetMapping("/me")
    public ResponseEntity<List<CheckoutDto>> getAllMyActiveCheckouts(Authentication authentication) {
        List<Checkout> checkouts = checkoutService.getMyActiveCheckouts(authentication.getName());
        List<CheckoutDto> checkoutDtos = checkouts.stream()
                .map(checkoutMapper::toDto)
                .toList();
        return ResponseEntity.ok(checkoutDtos);
    }
}
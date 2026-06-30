package com.CMTS.inventory.controller;


import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.dto.CheckoutDto;
import com.CMTS.inventory.domain.dto.CheckoutRequestDto;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.mapper.CheckoutMapper;
import com.CMTS.inventory.service.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkouts")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final CheckoutMapper checkoutMapper;

    public CheckoutController(CheckoutService checkoutService, CheckoutMapper checkoutMapper) {
        this.checkoutService = checkoutService;
        this.checkoutMapper = checkoutMapper;
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
}
package com.CMTS.inventory.mapper;

import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.dto.CheckoutDto;
import com.CMTS.inventory.domain.dto.CheckoutRequestDto;
import com.CMTS.inventory.domain.entity.Checkout;

public interface CheckoutMapper {

    CheckoutRequest fromDTO(CheckoutRequestDto dto);

    CheckoutDto toDto (Checkout checkout);
}

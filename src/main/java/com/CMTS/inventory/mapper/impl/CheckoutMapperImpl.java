package com.CMTS.inventory.mapper.impl;

import com.CMTS.inventory.domain.CheckoutRequest;
import com.CMTS.inventory.domain.dto.CheckoutDto;
import com.CMTS.inventory.domain.dto.CheckoutRequestDto;
import com.CMTS.inventory.domain.entity.Checkout;
import com.CMTS.inventory.mapper.CheckoutMapper;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapperImpl implements CheckoutMapper{
    @Override
    public CheckoutRequest fromDTO(CheckoutRequestDto dto) {
        return new CheckoutRequest(
                dto.itemId(),
                dto.userId(),
                dto.productionId()
        );
    }

    @Override
    public CheckoutDto toDto(Checkout checkout) {
        return new CheckoutDto(
                checkout.getId(),
                checkout.getItem().getId(),
                checkout.getUser().getId(),
                checkout.getProduction().getId(),
                checkout.getCheckedOutAt(),
                checkout.getDueDate(),
                checkout.getReturnedAt()
        );
    }
}

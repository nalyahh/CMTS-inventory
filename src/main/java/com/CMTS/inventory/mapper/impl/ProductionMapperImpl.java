package com.CMTS.inventory.mapper.impl;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.dto.CreateProductionRequestDto;
import com.CMTS.inventory.domain.dto.ProductionDto;
import com.CMTS.inventory.domain.entity.Production;
import com.CMTS.inventory.mapper.ProductionMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductionMapperImpl implements ProductionMapper {
    @Override
    public CreateProductionRequest fromDTO(CreateProductionRequestDto dto) {
        return new CreateProductionRequest(
                dto.name(),
                dto.startDate(),
                dto.endDate()
        );
    }

    @Override
    public ProductionDto toDto(Production production) {
        return new ProductionDto(
                production.getId(),
                production.getName(),
                production.getStartDate(),
                production.getEndDate()
        );
    }
}

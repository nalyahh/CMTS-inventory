package com.CMTS.inventory.mapper;

import com.CMTS.inventory.domain.CreateProductionRequest;
import com.CMTS.inventory.domain.dto.CreateProductionRequestDto;
import com.CMTS.inventory.domain.dto.ProductionDto;
import com.CMTS.inventory.domain.entity.Production;

public interface ProductionMapper {

    CreateProductionRequest fromDTO(CreateProductionRequestDto dto);

    ProductionDto toDto(Production production);
}

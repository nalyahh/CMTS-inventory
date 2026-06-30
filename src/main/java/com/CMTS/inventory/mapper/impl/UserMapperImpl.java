package com.CMTS.inventory.mapper.impl;

import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.dto.CreateUserRequestDto;
import com.CMTS.inventory.domain.dto.UserDto;
import com.CMTS.inventory.domain.entity.User;
import com.CMTS.inventory.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public CreateUserRequest fromDTO(CreateUserRequestDto dto) {
        return new CreateUserRequest(
                dto.name(),
                dto.email(),
                dto.password(),
                dto.role(),
                dto.productionId()
        );
    }

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getProduction() != null ? user.getProduction().getId() : null
        );
    }
}

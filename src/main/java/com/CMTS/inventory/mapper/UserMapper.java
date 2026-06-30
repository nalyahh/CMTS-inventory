package com.CMTS.inventory.mapper;


import com.CMTS.inventory.domain.CreateUserRequest;
import com.CMTS.inventory.domain.dto.CreateUserRequestDto;
import com.CMTS.inventory.domain.dto.UserDto;
import com.CMTS.inventory.domain.entity.User;

public interface UserMapper {

    CreateUserRequest fromDTO(CreateUserRequestDto dto);
    UserDto toDto(User user);
}

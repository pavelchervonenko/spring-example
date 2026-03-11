package io.hexlet.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.model.User;

@Mapper(componentModel = "spring")

public interface UserMapper {
    User toEntity(UserCreateDTO dto);
    User toEntity(UserDTO dto);
    UserDTO toDTO(User post);
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User post);
}
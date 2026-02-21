package io.hexlet.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.model.Post;

@Mapper(componentModel = "spring")

public interface PostMapper {
    Post toEntity(PostCreateDTO dto);
    PostDTO toDTO(Post post);
    void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post post);
}

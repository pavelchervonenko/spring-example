package io.hexlet.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.model.Post;

@Mapper(
    componentModel = "spring",
    uses = {JsonNullableMapper.class, ReferenceMapper.class}
)
public interface PostMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "tags", source = "tagsId")
    Post toEntity(PostCreateDTO dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "tagsId", source = "tags", qualifiedByName = "tagsToId")
    PostDTO toDTO(Post post);

    @Mapping(target = "tags", source = "tagsId")
    void updateEntityFromDTO(PostUpdateDTO dto, @MappingTarget Post post);
}

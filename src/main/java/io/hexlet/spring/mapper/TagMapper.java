package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {JsonNullableMapper.class, ReferenceMapper.class}
)
public interface TagMapper {
    @Mapping(target = "posts", ignore = true)
    Tag toEntity(TagCreateDTO dto);

    TagDTO toDTO(Tag tag);

    void updateEntityFromDTO(TagUpdateDTO dto, @MappingTarget Tag tag);
}

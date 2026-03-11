package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.model.Tag;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T17:49:02+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class TagMapperImpl implements TagMapper {

    @Override
    public Tag toEntity(TagCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Tag tag = new Tag();

        tag.setName( dto.getName() );

        return tag;
    }

    @Override
    public TagDTO toDTO(Tag tag) {
        if ( tag == null ) {
            return null;
        }

        TagDTO tagDTO = new TagDTO();

        tagDTO.setId( tag.getId() );
        tagDTO.setName( tag.getName() );

        return tagDTO;
    }

    @Override
    public void updateEntityFromDTO(TagUpdateDTO dto, Tag tag) {
        if ( dto == null ) {
            return;
        }

        tag.setName( dto.getName() );
    }
}

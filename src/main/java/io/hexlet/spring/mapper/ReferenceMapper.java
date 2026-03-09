package io.hexlet.spring.mapper;

import io.hexlet.spring.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import io.hexlet.spring.model.BaseEntity;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("tagToId")
    public Long tagToId(Tag tag) {
        return tag != null ? tag.getId() : null;
    }

    @Named("tagsToId")
    public List<Long> tagsToId(List<Tag> tags) {
        if (tags == null) return null;
        return tags.stream()
                .map(this::tagToId)
                .collect(Collectors.toList());
    }
}
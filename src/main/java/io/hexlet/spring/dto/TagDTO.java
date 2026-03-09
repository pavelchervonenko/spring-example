package io.hexlet.spring.dto;

import io.hexlet.spring.model.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagDTO {
    private Long id;
    private String name;
}

package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;
import net.datafaker.providers.base.Bool;

import java.time.LocalDateTime;


@Setter
@Getter
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean published;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}

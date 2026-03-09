package io.hexlet.spring.dto;

import io.hexlet.spring.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class PostDTO {
    private Long id;
    private List<Long> tagsId;
    private Long userId;
    private String title;
    private String content;
    private Boolean published;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}

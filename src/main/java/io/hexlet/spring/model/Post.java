package io.hexlet.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
public class Post {
    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Content must not be blank")
    private String content;

    private String author;
    private LocalDateTime createdAt;
}
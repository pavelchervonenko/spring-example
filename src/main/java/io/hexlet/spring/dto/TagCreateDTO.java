package io.hexlet.spring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateDTO {
    @NotBlank
    private String name;
}

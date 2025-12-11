package io.hexlet.spring.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class User {
    private Long id;
    private String name;

    @NotBlank(message = "Email must not be blank")
    private String email;
}

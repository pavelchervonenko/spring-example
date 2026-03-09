package io.hexlet.spring.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class TagPatchDTO {
    private JsonNullable<String> name = JsonNullable.undefined();
}

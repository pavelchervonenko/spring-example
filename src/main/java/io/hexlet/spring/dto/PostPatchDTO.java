package io.hexlet.spring.dto;

import org.openapitools.jackson.nullable.JsonNullable;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PostPatchDTO {
    private JsonNullable<String> title = JsonNullable.undefined();
    private JsonNullable<String> content = JsonNullable.undefined();
    private JsonNullable<Boolean> published = JsonNullable.undefined();
    private JsonNullable<List<Long>> tagsId = JsonNullable.undefined();
}

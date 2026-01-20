package io.hexlet.spring.util;

import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublished(post.isPublished());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }
}

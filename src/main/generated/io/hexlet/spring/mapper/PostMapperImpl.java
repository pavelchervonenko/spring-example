package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T18:04:35+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class PostMapperImpl extends PostMapper {

    @Override
    public Post map(PostCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Post post = new Post();

        post.setTitle( dto.getTitle() );
        post.setContent( dto.getContent() );

        return post;
    }

    @Override
    public PostDTO map(Post model) {
        if ( model == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setId( model.getId() );
        postDTO.setTitle( model.getTitle() );
        postDTO.setContent( model.getContent() );
        postDTO.setPublished( model.isPublished() );
        postDTO.setUpdatedAt( model.getUpdatedAt() );
        postDTO.setCreatedAt( model.getCreatedAt() );

        return postDTO;
    }

    @Override
    public void update(PostUpdateDTO dto, Post model) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getTitle() != null ) {
            model.setTitle( dto.getTitle() );
        }
        if ( dto.getContent() != null ) {
            model.setContent( dto.getContent() );
        }
    }
}

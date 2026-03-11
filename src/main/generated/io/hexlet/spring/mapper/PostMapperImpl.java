package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T17:49:02+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Autowired
    private ReferenceMapper referenceMapper;

    @Override
    public Post toEntity(PostCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Post post = new Post();

        post.setUser( postCreateDTOToUser( dto ) );
        post.setTags( longListToTagList( dto.getTagsId() ) );
        post.setTitle( dto.getTitle() );
        post.setContent( dto.getContent() );

        return post;
    }

    @Override
    public PostDTO toDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setUserId( postUserId( post ) );
        postDTO.setTagsId( referenceMapper.tagsToId( post.getTags() ) );
        postDTO.setId( post.getId() );
        postDTO.setTitle( post.getTitle() );
        postDTO.setContent( post.getContent() );
        postDTO.setPublished( post.isPublished() );
        postDTO.setUpdatedAt( post.getUpdatedAt() );
        postDTO.setCreatedAt( post.getCreatedAt() );

        return postDTO;
    }

    @Override
    public void updateEntityFromDTO(PostUpdateDTO dto, Post post) {
        if ( dto == null ) {
            return;
        }

        if ( post.getTags() != null ) {
            List<Tag> list = longListToTagList( dto.getTagsId() );
            if ( list != null ) {
                post.getTags().clear();
                post.getTags().addAll( list );
            }
            else {
                post.setTags( null );
            }
        }
        else {
            List<Tag> list = longListToTagList( dto.getTagsId() );
            if ( list != null ) {
                post.setTags( list );
            }
        }
        post.setTitle( dto.getTitle() );
        post.setContent( dto.getContent() );
    }

    protected User postCreateDTOToUser(PostCreateDTO postCreateDTO) {
        if ( postCreateDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( postCreateDTO.getUserId() );

        return user;
    }

    protected List<Tag> longListToTagList(List<Long> list) {
        if ( list == null ) {
            return null;
        }

        List<Tag> list1 = new ArrayList<Tag>( list.size() );
        for ( Long long1 : list ) {
            list1.add( referenceMapper.toEntity( long1, Tag.class ) );
        }

        return list1;
    }

    private Long postUserId(Post post) {
        if ( post == null ) {
            return null;
        }
        User user = post.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}

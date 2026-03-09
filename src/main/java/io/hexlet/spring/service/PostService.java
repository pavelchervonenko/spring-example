package io.hexlet.spring.service;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.specification.PostSpecification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostSpecification specBuilder;

    public List<PostDTO> getAll(PostParamsDTO params, int page) {
        var spec = specBuilder.build(params);

        var posts =  postRepository.findAll(spec, PageRequest.of(page, 10));

        return posts.stream()
                .map(postMapper::toDTO)
                .toList();
    }

    public PostDTO getById(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));

        return postMapper.toDTO(post);
    }

    public PostDTO createPost(PostCreateDTO dto) {
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Author " + dto.getUserId() + " Not Found"));

        Post post = postMapper.toEntity(dto);
        post.setUser(user);

        if (post.getTags() == null) {
            post.setTags(new ArrayList<>());
        } else {
            post.getTags().clear();
        }

        if (dto.getTagsId() != null && !dto.getTagsId().isEmpty()) {
            List<Tag> tags = dto.getTagsId().stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag with id: " + tagId + " not found")))
                    .collect(Collectors.toList());

            for (Tag tag : tags) {
                post.addTag(tag);
            }
        }

        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    public PostDTO updatePost(Long id, PostUpdateDTO dto) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));

        postMapper.updateEntityFromDTO(dto, post);

        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + dto.getUserId() + " not found"));
        post.setUser(user);

        if (dto.getTagsId() != null) {
            updatePostTags(post, dto.getTagsId());
        }

        postRepository.save(post);

        return postMapper.toDTO(post);
    }

    public PostDTO patchPost(Long id, PostPatchDTO dto) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));

        dto.getTitle().ifPresent(post::setTitle);
        dto.getContent().ifPresent(post::setContent);
        dto.getPublished().ifPresent(post::setPublished);

        if (dto.getTagsId() != null && dto.getTagsId().isPresent()) {
            updatePostTags(post, dto.getTagsId().get());
        }

        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    public void deletePost(Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id: " + id + " not found"));
        post.getTags().clear();
        postRepository.delete(post);
    }



    private void updatePostTags(Post post, List<Long> newTagsId) {
        if (newTagsId == null || newTagsId.isEmpty()) {
            post.getTags().clear();
            return;
        }

        List<Tag> newTags = tagRepository.findAllById(newTagsId);

        if (newTags.size() != newTagsId.size()) {
            Set<Long> foundIds = newTags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toSet());

            String missingTags = newTagsId.stream()
                    .filter(id -> !foundIds.contains(id))
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            throw new ResourceNotFoundException("Tags not found: " + missingTags);
        }

        post.getTags().clear();
        post.getTags().addAll(newTags);
    }

}

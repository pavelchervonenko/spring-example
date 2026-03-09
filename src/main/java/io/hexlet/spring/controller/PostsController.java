package io.hexlet.spring.controller;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;

import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.PostRepository;

import io.hexlet.spring.mapper.PostMapper;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostMapper postMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO> index(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var posts =  postRepository.findByPublishedTrue(pageable);

        return posts.stream()
                .map(postMapper::toDTO)
                .toList();

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));

        return postMapper.toDTO(post);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@Valid @RequestBody PostCreateDTO dto) {
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
                            .orElseThrow(() -> new ResourceNotFoundException("Tag " + tagId + " Not Found")))
                    .collect(Collectors.toList());

            for (Tag tag : tags) {
                post.addTag(tag);
            }
        }

        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    @PutMapping("/{id}")
    public PostDTO update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PostUpdateDTO dto) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " Not Found"));

        postMapper.updateEntityFromDTO(dto, post);

        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Author " + dto.getUserId() + " Not Found"));
        post.setUser(user);

        if (dto.getTagsId() != null) {
            updatePostTags(post, dto.getTagsId());
        }

        postRepository.save(post);

        return postMapper.toDTO(post);
    }

    @PatchMapping("/{id}")
    public PostDTO patch(
            @PathVariable("id") Long id,
            @RequestBody PostPatchDTO dto) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " Not Found"));

        dto.getTitle().ifPresent(post::setTitle);
        dto.getContent().ifPresent(post::setContent);
        dto.getPublished().ifPresent(post::setPublished);

        if (dto.getTagsId() != null && dto.getTagsId().isPresent()) {
            updatePostTags(post, dto.getTagsId().get());
        }

        postRepository.save(post);
        return postMapper.toDTO(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " Not Found"));
        post.getTags().clear();
        postRepository.delete(post);
    }

    private void updatePostTags(Post post, List<Long> newTagsId) {
        List<Tag> oldTags = List.copyOf(post.getTags());
        for (Tag tag : oldTags) {
            post.removeTag(tag);
        }

        if (newTagsId != null && !newTagsId.isEmpty()) {
            List<Tag> newTags = newTagsId.stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag " + tagId + " Not Found")))
                    .collect(Collectors.toList());

            for (Tag tag : newTags) {
                post.addTag(tag);
            }
        }
    }
}

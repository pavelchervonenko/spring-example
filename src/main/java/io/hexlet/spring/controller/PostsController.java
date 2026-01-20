package io.hexlet.spring.controller;

import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.model.Post;

import io.hexlet.spring.repository.PostRepository;

import io.hexlet.spring.util.PostMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO> index(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var posts =  postRepository.findByPublishedTrue(pageable);

        var result = posts.stream()
                .map(postMapper::toDTO)
                .toList();
        return result;

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));

        var result = postMapper.toDTO(post);
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@Valid @RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable("id") Long id,
                                       @RequestBody Post data) {
        var post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post " + id + " Not Found"));

        post.setTitle(data.getTitle());
        post.setContent(data.getContent());
        post.setPublished(data.isPublished());

        return postRepository.save(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") Long id) {
        postRepository.deleteById(id);
    }
}

package io.hexlet.spring.controller;

import io.hexlet.spring.dto.PostCreateDTO;
import io.hexlet.spring.dto.PostDTO;
import io.hexlet.spring.dto.PostParamsDTO;
import io.hexlet.spring.dto.PostPatchDTO;
import io.hexlet.spring.dto.PostUpdateDTO;

import io.hexlet.spring.specification.PostSpecification;

import io.hexlet.spring.service.PostService;

import io.hexlet.spring.mapper.PostMapper;

import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import io.hexlet.spring.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {
    @Autowired
    private PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostDTO> index(PostParamsDTO params, @RequestParam(defaultValue = "0") int page) {
        return postService.getAll(params, page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO show(@PathVariable Long id) {
        return postService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO create(@Valid @RequestBody PostCreateDTO dto) {
        return postService.createPost(dto);
    }

    @PutMapping("/{id}")
    public PostDTO update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PostUpdateDTO dto) {
        return postService.updatePost(id, dto);
    }

    @PatchMapping("/{id}")
    public PostDTO patch(
            @PathVariable("id") Long id,
            @RequestBody PostPatchDTO dto) {
        return postService.patchPost(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
    }
}

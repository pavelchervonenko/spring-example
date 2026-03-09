package io.hexlet.spring.controller;

import io.hexlet.spring.dto.TagDTO;
import io.hexlet.spring.dto.TagCreateDTO;
import io.hexlet.spring.dto.TagUpdateDTO;
import io.hexlet.spring.dto.TagPatchDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;
import io.hexlet.spring.mapper.TagMapper;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.model.Tag;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.TagRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagMapper tagMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDTO> getAllTags() {
        var tags = tagRepository.findAll();

        return tags.stream()
                .map(tagMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TagDTO show(@PathVariable Long id) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));

        return tagMapper.toDTO(tag);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO create(@Valid @RequestBody TagCreateDTO dto) {
        var tag = tagMapper.toEntity(dto);
        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }

    @PutMapping("/{id}")
    public TagDTO update(@PathVariable("id") Long id,
                          @Valid @RequestBody TagUpdateDTO dto) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        tagMapper.updateEntityFromDTO(dto, tag);
        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }

    @PatchMapping("/{id}")
    public TagDTO patch(
            @PathVariable("id") Long id,
            @RequestBody TagPatchDTO dto) {
        var tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag " + id + " Not Found"));

        dto.getName().ifPresent(tag::setName);

        tagRepository.save(tag);
        return tagMapper.toDTO(tag);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        for (Post post : new ArrayList<>(tag.getPosts())) {
            post.removeTag(tag);
            postRepository.save(post);
        }

        tagRepository.delete(tag);
    }
}

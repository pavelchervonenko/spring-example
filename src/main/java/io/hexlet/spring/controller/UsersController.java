package io.hexlet.spring.controller;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserPatchDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.exception.ResourceNotFoundException;

import io.hexlet.spring.mapper.UserMapper;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.model.Post;
import io.hexlet.spring.repository.PostRepository;
import io.hexlet.spring.repository.UserRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();

        return users.stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));

        return userMapper.toDTO(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO dto) {
        var user = userMapper.toEntity(dto);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable("id") Long id,
                                       @Valid @RequestBody UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        userMapper.updateEntityFromDTO(dto, user);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @PatchMapping("/{id}")
    public UserDTO patch(
            @PathVariable("id") Long id,
            @RequestBody UserPatchDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " Not Found"));

        dto.getFirstName().ifPresent(user::setFirstName);
        dto.getLastName().ifPresent(user::setLastName);
        dto.getEmail().ifPresent(user::setEmail);

        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " Not Found"));

        // просто отвязываем связь
        for (Post post : user.getPosts()) {
                 post.setUser(null);
            }

        userRepository.delete(user);
    }
}

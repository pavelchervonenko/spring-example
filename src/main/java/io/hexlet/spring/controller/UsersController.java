package io.hexlet.spring.controller;

import io.hexlet.spring.model.User;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private List<User> users = new ArrayList<>();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return users.stream().toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createdUser(@Valid @RequestBody User user) {
        users.add(user);

        return user;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        users.removeIf(u -> Objects.equals(u.getId(), id));
    }
}

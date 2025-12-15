package io.hexlet.spring.controller;

import io.hexlet.spring.exception.ResourceNotFoundException;

import io.hexlet.spring.model.User;

import io.hexlet.spring.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        var users = userRepository.findAll();
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + " Not Found"));
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable("id") Long id,
                                       @RequestBody User data) {
        var maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = maybeUser.get();
        user.setEmail(data.getEmail());
        user.setLastName(data.getLastName());
        user.setFirstName(data.getFirstName());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
    }
}

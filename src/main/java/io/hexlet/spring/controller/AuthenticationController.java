package io.hexlet.spring.controller;

import io.hexlet.spring.dto.UserRequestRegDTO;
import io.hexlet.spring.dto.UserResponseRegDTO;
import io.hexlet.spring.exception.UserAlreadyExistsException;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.service.CustomUserDetailsService;
import io.hexlet.spring.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.hexlet.spring.dto.AuthRequest;
import io.hexlet.spring.util.JWTUtils;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/login")
    public String auth(@RequestBody AuthRequest dto) {
        var authentication = new UsernamePasswordAuthenticationToken(
                dto.getUsername(),
                dto.getPassword());

        authenticationManager.authenticate(authentication);

        var token = jwtUtils.generateToken(dto.getUsername());
        return token;
    }

    @PostMapping("/reg")
    public ResponseEntity<?> create(@Valid @RequestBody UserRequestRegDTO dto) {
        try {
            UserResponseRegDTO response = userRegistrationService.registration(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed: " + e.getMessage());
        }
    }
}
package io.hexlet.spring.service;

import io.hexlet.spring.dto.UserRequestRegDTO;
import io.hexlet.spring.dto.UserResponseRegDTO;
import io.hexlet.spring.exception.UserAlreadyExistsException;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repository.UserRepository;
import io.hexlet.spring.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    public UserResponseRegDTO registration(UserRequestRegDTO dto) {
        if (userRepository.findByEmail(dto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + dto.getUsername() + " already exists");
        }
        var user = new User();
        user.setEmail(dto.getUsername());

        var hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPasswordDigest(hashedPassword);

        User savedUser = userRepository.save(user);

        String token = jwtUtils.generateToken(savedUser.getEmail());

        return new UserResponseRegDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                token
        );
    }
}

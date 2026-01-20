package io.hexlet.spring.util;

import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

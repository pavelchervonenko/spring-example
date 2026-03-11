package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-12T00:06:18+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );
        user.setEmail( dto.getEmail() );
        user.setPasswordDigest( dto.getPasswordDigest() );

        return user;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setId( dto.getId() );
        user.setFirstName( dto.getFirstName() );
        user.setLastName( dto.getLastName() );

        return user;
    }

    @Override
    public UserDTO toDTO(User post) {
        if ( post == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( post.getId() );
        userDTO.setUsername( post.getUsername() );
        userDTO.setFirstName( post.getFirstName() );
        userDTO.setLastName( post.getLastName() );
        userDTO.setPassword( post.getPassword() );

        return userDTO;
    }

    @Override
    public void updateEntityFromDTO(UserUpdateDTO dto, User post) {
        if ( dto == null ) {
            return;
        }

        post.setFirstName( dto.getFirstName() );
        post.setLastName( dto.getLastName() );
    }
}

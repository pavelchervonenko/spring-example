package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.UserCreateDTO;
import io.hexlet.spring.dto.UserDTO;
import io.hexlet.spring.dto.UserUpdateDTO;
import io.hexlet.spring.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-22T17:11:41+0300",
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

        user.setEmail( dto.getEmail() );
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
        userDTO.setEmail( post.getEmail() );
        userDTO.setFirstName( post.getFirstName() );
        userDTO.setLastName( post.getLastName() );

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

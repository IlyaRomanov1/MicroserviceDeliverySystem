package com.ilya.orderservice.services;

import com.ilya.orderservice.dtos.UserDto;
import com.ilya.orderservice.models.User;
import com.ilya.orderservice.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_shouldEncodePasswordAndSetRole(){
        //Arrange
        UserDto dto = new UserDto();
        dto.setEmail("test@mail.com");
        dto.setPassword("12345");

        User user = new User();
        user.setEmail("test@mail.com");

        when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");
        when(modelMapper.map(dto, User.class)).thenReturn(user);

        // Act
        userService.register(dto);

        // Assert
        assertEquals("ROLE_USER", dto.getRole());
        assertEquals("encodedPassword", dto.getPassword());
        verify(usersRepository).save(user);
    }
}

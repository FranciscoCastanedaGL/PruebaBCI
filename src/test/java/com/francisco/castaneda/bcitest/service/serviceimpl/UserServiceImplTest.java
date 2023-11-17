package com.francisco.castaneda.bcitest.service.serviceimpl;

import com.francisco.castaneda.bcitest.exceptions.ValidationsException;
import com.francisco.castaneda.bcitest.mapper.UserMapper;
import com.francisco.castaneda.bcitest.model.dto.InfoUserTokenDTO;
import com.francisco.castaneda.bcitest.model.dto.ResponseUserDTO;
import com.francisco.castaneda.bcitest.model.dto.UserDTO;
import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import com.francisco.castaneda.bcitest.security.JWTRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTRepository jwtRepository;

    @Mock
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
    }


    @Test
    void createUser_ValidUser_ShouldReturnInfoUserTokenDTO() {

        User newUser = User.builder()
                .email("test@example.com")
                .password("Francisco12.")
                .isActive(true).build();

        String mockToken = "mockedJWT";
        when(jwtRepository.create(newUser.getEmail())).thenReturn(mockToken);
        lenient().when(passwordEncoder.encode(newUser.getPassword())).thenReturn("hashedPassword");
        when(userRepository.findUserByEmailAndIsActive(newUser.getEmail(), newUser.getIsActive())).thenReturn(Optional.empty());

        Authentication auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(auth.isAuthenticated()).thenReturn(true);


        InfoUserTokenDTO result = userService.createUser(newUser);


        assertNotNull(result);
        assertEquals("Bearer ".concat(mockToken), result.getToken());
        assertEquals(newUser.getName(), result.getUserName());


        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void createUser_InvalidEmail_ShouldThrowValidationsException() {
        User newUser = User.builder()
                .email("")
                .password("Francisco12.")
                .isActive(true).build();

        assertThrows(ValidationsException.class, () -> userService.createUser(newUser));
    }

    @Test
    void createUser_InvalidPassword_ShouldThrowValidationsException() {
        User newUser = new User();
        newUser.setEmail("test@example.com");
        newUser.setPassword("short");
        newUser.setIsActive(true);


        assertThrows(ValidationsException.class, () -> userService.createUser(newUser));
    }

    @Test
    void signIn_ValidCredentials_ShouldReturnResponseUserDTO() {

        UserDTO userRequest =   UserDTO.builder()
                .email("valid@example.com")
                .password("Francisco12.").build();

        User userInDatabase = new User();
        userInDatabase.setEmail("valid@example.com");
        userInDatabase.setPassword("$2a$10$zv5EJvlwwTVtFbAakwXiZuEeyqe.uK59Uk5BWazprHSimI5rF9e.S");
        userInDatabase.setIsActive(true);

        ResponseUserDTO response = ResponseUserDTO.builder()
                .name("test")
                .isAdmin(true)
                .email("test@example.com")
                .token("mockedToken")
                .build();


        when(userRepository.findUserByEmailAndIsActive(eq("valid@example.com"), eq(true)))
                .thenReturn(Optional.of(userInDatabase));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(jwtRepository.create(eq("valid@example.com"))).thenReturn("mockedToken");


        when(userRepository.save(any(User.class))).thenReturn(userInDatabase);
        when(userMapper.mapUserToResponseUserDTO(any(User.class)))
                .thenReturn(response);

        ResponseUserDTO responseUserDTO = userService.sigIn(userRequest);

        assertNotNull(responseUserDTO);
        assertEquals("test@example.com", responseUserDTO.getEmail());
        assertNotNull(responseUserDTO.getToken());

        verify(userRepository, times(1)).save(userInDatabase);
    }
}
package com.francisco.castaneda.bcitest.service.serviceimpl;

import com.francisco.castaneda.bcitest.model.entity.User;
import com.francisco.castaneda.bcitest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserDetailsServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsername() {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword("hashedPassword");
        when(userRepository.findUserByEmail(email)).thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertAll("UserDetails properties",
                () -> assertNotNull(userDetails),
                () -> assertEquals(email, userDetails.getUsername()),
                () -> assertEquals("hashedPassword", userDetails.getPassword()),
                () -> assertTrue(userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ADMIN")))
        );

        verify(userRepository, times(1)).findUserByEmail(email);
    }

    @Test
    void loadUserByUsername_NonExistingUser_ShouldThrowException() {

        String nonExistingEmail = "nonexistent@example.com";
        when(userRepository.findUserByEmail(nonExistingEmail)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(nonExistingEmail);
        });

        verify(userRepository, times(1)).findUserByEmail(nonExistingEmail);
    }
}
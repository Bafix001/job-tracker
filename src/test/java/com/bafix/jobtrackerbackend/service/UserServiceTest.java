package com.bafix.jobtrackerbackend.service;

import com.bafix.jobtrackerbackend.dto.RegisterRequestDto;
import com.bafix.jobtrackerbackend.dto.UserResponseDto;
import com.bafix.jobtrackerbackend.entity.User;
import com.bafix.jobtrackerbackend.exception.EmailAlreadyUsedException;
import com.bafix.jobtrackerbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_success() {
        RegisterRequestDto dto = new RegisterRequestDto("alice","alice@example.com","password123");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponseDto resp = userService.register(dto);

        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getEmail()).isEqualTo(dto.getEmail());
        assertThat(resp.getPseudo()).isEqualTo(dto.getPseudo());
    }

    @Test
    void register_emailAlreadyExists_throws() {
        RegisterRequestDto dto = new RegisterRequestDto("bob","bob@example.com","password123");
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class, () -> userService.register(dto));
    }
}

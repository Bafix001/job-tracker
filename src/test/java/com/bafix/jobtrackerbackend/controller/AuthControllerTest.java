package com.bafix.jobtrackerbackend.controller;

import com.bafix.jobtrackerbackend.dto.RegisterRequestDto;
import com.bafix.jobtrackerbackend.dto.UserResponseDto;
import com.bafix.jobtrackerbackend.service.UserService;
import com.bafix.jobtrackerbackend.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Mock
    private com.bafix.jobtrackerbackend.security.JwtService jwtService;

    @Mock
    private com.bafix.jobtrackerbackend.service.auth.RefreshTokenService refreshTokenService;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthController controller = new AuthController(userService, authenticationManager, jwtService, refreshTokenService, passwordEncoder);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void register_endpoint_success() throws Exception {
        // use a raw JSON string to avoid needing jackson in test classpath
        String json = "{\"pseudo\":\"anna\",\"email\":\"anna@example.com\",\"password\":\"password123\"}";

        UserResponseDto resp = new UserResponseDto();
        resp.setId(1L);
        resp.setEmail("anna@example.com");
        resp.setPseudo("anna");

        when(userService.register(any(RegisterRequestDto.class))).thenReturn(resp);

        mockMvc.perform(post("/api/auth/register")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }
}

package com.bafix.jobtrackerbackend.controller;

import com.bafix.jobtrackerbackend.dto.LoginRequestDto;
import com.bafix.jobtrackerbackend.dto.RegisterRequestDto;
import com.bafix.jobtrackerbackend.dto.UserResponseDto;
import com.bafix.jobtrackerbackend.entity.User;
import com.bafix.jobtrackerbackend.security.JwtService;
import com.bafix.jobtrackerbackend.service.UserService;
import com.bafix.jobtrackerbackend.service.auth.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegisterRequestDto request){
        UserResponseDto response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequestDto request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userService.findByEmail(request.getEmail());
        String access = jwtService.generateAccessToken(user);
        String refresh = refreshTokenService.createRefreshToken(user).getToken();
        Map<String,Object> body = new HashMap<>();
        body.put("accessToken", access);
        body.put("refreshToken", refresh);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,String>> refresh(@RequestBody Map<String,String> body){
        String refresh = body.get("refreshToken");
        var tokenObj = refreshTokenService.findByToken(refresh);
        if(tokenObj == null || !refreshTokenService.isValid(tokenObj)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = tokenObj.getUser();
        String access = jwtService.generateAccessToken(user);
        Map<String,String> resp = new HashMap<>();
        resp.put("accessToken", access);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String,String> body){
        String refresh = body.get("refreshToken");
        var tokenObj = refreshTokenService.findByToken(refresh);
        if(tokenObj != null){
            refreshTokenService.deleteByUserId(tokenObj.getUser().getId());
        }
        return ResponseEntity.noContent().build();
    }
}

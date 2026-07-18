package com.bafix.jobtrackerbackend.service;

import com.bafix.jobtrackerbackend.dto.RegisterRequestDto;
import com.bafix.jobtrackerbackend.dto.UserResponseDto;
import com.bafix.jobtrackerbackend.entity.User;
import com.bafix.jobtrackerbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bafix.jobtrackerbackend.exception.EmailAlreadyUsedException;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto register(RegisterRequestDto registerRequestDto){
        boolean emailExists = userRepository.existsByEmail(registerRequestDto.getEmail());
        if(emailExists){
            throw new EmailAlreadyUsedException("Cet email est déjà utilisé");
        }
        String hashedPassword = passwordEncoder.encode(registerRequestDto.getPassword());
        User user = new User();
        user.setPseudo(registerRequestDto.getPseudo());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(hashedPassword);
        user.setCreatedAt(LocalDate.now());

        User savedUser;
        try{
            savedUser = userRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            log.warn("Failed to save user, possible duplicate email: {}", registerRequestDto.getEmail(), ex);
            throw new EmailAlreadyUsedException("Cet email est déjà utilisé");
        }

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setPseudo(savedUser.getPseudo());
        userResponseDto.setId(savedUser.getId());
        userResponseDto.setEmail(savedUser.getEmail());
        userResponseDto.setCreatedAt(savedUser.getCreatedAt());

        return userResponseDto;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

}

package com.movieflix.movieAPI.auth.service;


import com.movieflix.movieAPI.auth.entities.User;
import com.movieflix.movieAPI.auth.entities.UserRole;
import com.movieflix.movieAPI.auth.repositories.UserRepository;
import com.movieflix.movieAPI.auth.utils.AuthResponse;
import com.movieflix.movieAPI.auth.utils.LoginRequest;
import com.movieflix.movieAPI.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthControllerService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest){
        var user= User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .build();
        User savedUser = userRepository.save(user);
        Map<String,Object> mp;
        var accessToken=jwtService.generateToken(new HashMap<>(),savedUser);
        var refreshToken=refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found with email:" + loginRequest.getEmail()));
        var accessToken =jwtService.generateToken(new HashMap<>(),user);
        var refreshToken=refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .refreshToken(refreshToken.getRefreshToken())
                .accessToken(accessToken)
                .build();
    }
}

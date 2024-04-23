package com.movieflix.movieAPI.auth.controller;

import com.movieflix.movieAPI.auth.entities.RefreshToken;
import com.movieflix.movieAPI.auth.entities.User;
import com.movieflix.movieAPI.auth.service.AuthControllerService;
import com.movieflix.movieAPI.auth.service.JwtService;
import com.movieflix.movieAPI.auth.service.RefreshTokenService;
import com.movieflix.movieAPI.auth.utils.AuthResponse;
import com.movieflix.movieAPI.auth.utils.LoginRequest;
import com.movieflix.movieAPI.auth.utils.RefreshTokenRequest;
import com.movieflix.movieAPI.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthControllerService authControllerService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authControllerService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authControllerService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken=refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        System.out.println(user);
        String accessToken=jwtService.generateToken(new HashMap<>(),user);

        return ResponseEntity.ok(AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build());
    }

}

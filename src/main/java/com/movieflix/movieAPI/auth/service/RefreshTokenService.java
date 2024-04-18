package com.movieflix.movieAPI.auth.service;

import com.movieflix.movieAPI.auth.entities.RefreshToken;
import com.movieflix.movieAPI.auth.entities.User;
import com.movieflix.movieAPI.auth.repositories.RefreshTokenRepository;
import com.movieflix.movieAPI.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User does not found with email: "+email));
        RefreshToken refreshToken=user.getRefreshToken();

        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*1000;
            refreshToken=RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken=refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()-> new RuntimeException("Refresh token not found"));

        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token expired");
        }
        return refToken;
    }


}

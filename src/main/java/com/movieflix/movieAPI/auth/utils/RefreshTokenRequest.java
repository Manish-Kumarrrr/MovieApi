package com.movieflix.movieAPI.auth.utils;


import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}

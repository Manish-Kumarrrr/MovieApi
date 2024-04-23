package com.movieflix.movieAPI.auth.forgotResetPassword.dto;


import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}

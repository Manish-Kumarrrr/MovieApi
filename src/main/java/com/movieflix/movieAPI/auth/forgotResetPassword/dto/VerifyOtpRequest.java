package com.movieflix.movieAPI.auth.forgotResetPassword.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VerifyOtpRequest {
    private Integer otp;
    @Email(message = "Enter a valid email")
    private String email;
}

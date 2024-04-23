package com.movieflix.movieAPI.auth.forgotResetPassword.dto;

import lombok.Builder;
import lombok.Data;

public record ChangePassword(String password,String repeatPassword ,String email) {

}

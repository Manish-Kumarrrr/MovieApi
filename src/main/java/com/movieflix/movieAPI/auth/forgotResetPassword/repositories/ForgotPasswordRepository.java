package com.movieflix.movieAPI.auth.forgotResetPassword.repositories;

import com.movieflix.movieAPI.auth.entities.User;
import com.movieflix.movieAPI.auth.forgotResetPassword.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;



public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("Select fp from ForgotPassword fp  where fp.otp=?1 and fp.user=?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    Optional<ForgotPassword> findByUser(User user);


}

package com.movieflix.movieAPI.auth.forgotResetPassword.controller;


import com.movieflix.movieAPI.auth.entities.User;
import com.movieflix.movieAPI.auth.forgotResetPassword.dto.ChangePassword;
import com.movieflix.movieAPI.auth.forgotResetPassword.dto.MailBody;
import com.movieflix.movieAPI.auth.forgotResetPassword.dto.VerifyOtpRequest;
import com.movieflix.movieAPI.auth.forgotResetPassword.entities.ForgotPassword;
import com.movieflix.movieAPI.auth.forgotResetPassword.repositories.ForgotPasswordRepository;
import com.movieflix.movieAPI.auth.forgotResetPassword.service.EmailService;
import com.movieflix.movieAPI.auth.repositories.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;


@RequiredArgsConstructor
@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordConroller {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    // send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> vefifyMail(@PathVariable String email){
        User user=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Please provide a valid email"));
        int otp=otpGenerator();
        MailBody mailBody =MailBody
                .builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forget Password request")
                .build();

        ForgotPassword forgotPassword=ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        return ResponseEntity.ok("Email sent for Verification!");

    }


    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest){
        User user=userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(()-> new UsernameNotFoundException("Please provide an Valid email!"));
        ForgotPassword forgotPassword=forgotPasswordRepository.findByOtpAndUser(verifyOtpRequest.getOtp(),user)
                .orElseThrow(()-> new RuntimeException("Invalid OTP for email: "+verifyOtpRequest.getEmail()));

        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
            return new ResponseEntity<>("OTP has been expired!", HttpStatus.EXPECTATION_FAILED);
        }
            forgotPasswordRepository.deleteById(forgotPassword.getFpid());
        return ResponseEntity.ok("OTP has been verified!");

    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again!",HttpStatus.EXPECTATION_FAILED);
        }

        String password=passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(changePassword.email(),changePassword.password());
        return ResponseEntity.ok("Password has been changed!");


    }
    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100000,999999);
    }
}

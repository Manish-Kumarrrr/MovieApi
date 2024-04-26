package com.movieflix.movieAPI.auth.forgotResetPassword.service;

import com.movieflix.movieAPI.auth.forgotResetPassword.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    public String from;
    public void sendSimpleMessage(MailBody mailBody){

        System.out.println("message service Start");
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo("mkc1020304050@gmail.com");
        message.setFrom(from);
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());
        javaMailSender.send(message);

        System.out.println("message service End");
    }

}

package com.movieflix.movieAPI.auth.forgotResetPassword.service;

import com.movieflix.movieAPI.auth.forgotResetPassword.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);

    }

}

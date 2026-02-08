package com.example.The.Eternal.Appetite.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;


    @PostConstruct
    public void checkEnv() {
        System.out.println("USERNAME from env: " + System.getenv("EMAIL_USERNAME"));
        System.out.println("PASSWORD from env: " + System.getenv("EMAIL_PASSWORD"));
        System.out.println("USERNAME from @Value: " + username);
        System.out.println("PASSWORD from @Value: " + password);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Dear user,\n\nYour OTP for resetting your password is: " + otp +
                        "\n\nPlease use this OTP to proceed. \n\nRegards,\nThe Eternal Appetite Team");
        mailSender.send(message);
    }
}

package com.zinios.onboard.service;

import com.zinios.onboard.exception.ZiniosException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private void sendEmail(String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        }
        catch (MessagingException e) {
            throw new ZiniosException("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendPasswordResetLink(String email, String otp, String resetLink) {
        String subject = "Password Reset Request";
        String body = "You have requested a password reset. Please use the following OTP and link to reset your password:<br><br>"
                        + "<strong>OTP: " + otp + "</strong><br>"
                        + "<strong>Reset Link:</strong> <a href=\"" + resetLink + "\">" + resetLink + "</a><br><br>";
        sendEmail(email, subject, body);
    }
}

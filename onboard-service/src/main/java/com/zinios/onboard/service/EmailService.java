package com.zinios.onboard.service;

import com.zinios.onboard.exception.ZiniosException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            helper.setFrom(new InternetAddress(fromEmail, "ZiniosEdge"));
            helper.setTo(email);
            String plain = htmlToPlain(body);
            helper.setSubject(subject);
            helper.setText(plain, body);
            mailSender.send(message);
        }
        catch (MessagingException e) {
            throw new ZiniosException("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            throw new ZiniosException("Not Supported", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public void sendPasswordResetLink(String email, String otp, String resetLink) {
        String subject = "Password Reset Request";
        String body = "You have requested a password reset. Please use the following OTP and link to reset your password:<br><br>"
                        + "<strong>OTP: " + otp + "</strong><br>"
                        + "<strong>Reset Link:</strong> <a href=\"" + resetLink + "\">" + resetLink + "</a><br><br>";
        sendEmail(email, subject, body);
    }

    private String htmlToPlain(String html) {
        // super simple fallback; replace with a better HTML->text converter if needed
        return html.replaceAll("<br\\s*/?>", "\n").replaceAll("<[^>]+>", "").trim();
    }

    private String escape(String s) {
        // minimal HTML escaping for any user-supplied values
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String escapeUrl(String s) { return s == null ? "" : s; }
}

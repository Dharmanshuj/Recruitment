package com.zinios.onboard.service;

import com.zinios.onboard.DTO.ChangePasswordRequest;
import com.zinios.onboard.DTO.ForgotPasswordRequest;
import com.zinios.onboard.DTO.LoginRequest;
import com.zinios.onboard.DTO.ResetPasswordRequest;
import com.zinios.onboard.Entity.Otp;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Mapper.UserMapper;
import com.zinios.onboard.Repository.OtpRepository;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtUtil;
    private final EmailService emailService;
    private final OtpRepository otpRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public String authenticateUserAndGenerateToken(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ZiniosException("User not found with email: " + loginRequest.email(), HttpStatus.BAD_REQUEST));
        if(!user.isActive()) {
            throw new ZiniosException("User account is inactive", HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (AuthenticationException e) {
            throw new ZiniosException("Invalid Username or Password", HttpStatus.UNAUTHORIZED, e);
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    public void forgetPassword(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ZiniosException("User not found with email: " + username, HttpStatus.BAD_REQUEST));
        if(!user.isActive()) {
            throw new ZiniosException("User account is inactive", HttpStatus.BAD_REQUEST);
        }
        otpRepository.deactivateAllActiveOtpsForEmail(username);
        Random random = new Random();
        int otpGenerated = 1000 + random.nextInt(9000);
        String otpValue = String.valueOf(otpGenerated);
        System.out.println(otpValue);
        Otp otp = mapper.generateOtp(otpValue, username);
        otpRepository.save(otp);
        String link = "https://localhost:8082/api/auth/resetPassword";
        emailService.sendPasswordResetLink(username, otpValue, link);
    }

    public void resetPassword(ResetPasswordRequest request) {
        Otp otp = otpRepository.findTopByEmailAndOtpAndIsActiveTrueOrderByCreatedTimeDesc(request.email(), request.otp())
                .orElseThrow(() -> new ZiniosException("Please enter a valid OTP", HttpStatus.BAD_GATEWAY));
        if(otp.isValidated() || otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otp.setActive(false);
            otpRepository.save(otp);
            throw new ZiniosException("OTP is already Validated or Expired", HttpStatus.BAD_GATEWAY);
        }
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ZiniosException("User not found with email: " + request.email(), HttpStatus.BAD_REQUEST));
        if(!user.isActive()) {
            throw new ZiniosException("User account is inactive", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        otp.setActive(false);
        otp.setValidated(true);
        otpRepository.save(otp);
    }
}

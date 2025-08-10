package com.zinios.onboard.controller;

import com.zinios.onboard.DTO.*;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import com.zinios.onboard.service.AuthService;
import com.zinios.onboard.service.InviteService;
import com.zinios.onboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserService userService;
    private final InviteService inviteService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    public ResponseEntity<JWTResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String jwt = authService.authenticateUserAndGenerateToken(loginRequest);
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ZiniosException("User not found after authentication", HttpStatus.BAD_REQUEST));
        return ResponseEntity.ok(new JWTResponse(
                jwt,
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType().name(),
                user.isActive()
        ));
    }

    @PostMapping("/register")
    @Operation(summary = "Register", description = "Register a new user")
    public ResponseEntity<User> register(@Valid @RequestBody UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = authentication.getName();

        User newUser = userService.registerUser(userRequest, createdBy);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/forgetPassword")
    @Operation(summary = "Forget Password", description = "Forgot Password")
    public ResponseEntity<String> forgetPass(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        authService.forgetPassword(forgotPasswordRequest.email());
        return ResponseEntity.ok("Your OTP and Password Reset Link is sent to your email successfully");
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset Password", description = "Reset the password with otp")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("Password is reset successfully");
    }

    @PutMapping("respond/{invite_id}/{status}")
    @Operation(summary = "Respond", description = "Accept or Reject the opportunity")
    public ResponseEntity<String> respondToInvite(@PathVariable("invite_id") Long inviteId, @PathVariable("status") String status, @RequestParam String password) {
        boolean updated = inviteService.updateStatus(inviteId, status, password);
        if(!updated) {
            return ResponseEntity.badRequest().body("Invalid invite ID or status");
        }
        return ResponseEntity.ok("Invite status updated to: " + status);
    }
}
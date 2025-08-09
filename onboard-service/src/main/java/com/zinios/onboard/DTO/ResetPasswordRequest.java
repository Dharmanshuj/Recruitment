package com.zinios.onboard.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "New password cannot be blank")
        @Size(min = 6, message = "Password must be at least 6 characters long")
        String newPassword,

        @NotBlank(message = "OTP cannot be blank")
        String otp) {}

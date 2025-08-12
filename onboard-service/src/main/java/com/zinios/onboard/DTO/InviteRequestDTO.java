package com.zinios.onboard.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InviteRequestDTO {
    @NotBlank(message = "Candidates email is required")
    @Email(message = "Enter a valid email address")
    private String candidateEmail;

    @NotBlank(message = "Recruiter Id is required")
    private Long recruiterId;
}


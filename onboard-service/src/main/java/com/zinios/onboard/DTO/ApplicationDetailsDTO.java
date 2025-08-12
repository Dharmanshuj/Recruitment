package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.ApplicationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationDetailsDTO {

    @NotNull(message = "candidateId is required")
    private Long candidateId;

    @NotBlank(message = "clientName is required")
    private String clientName;

    // allow today or future join date; drop this if you also allow past dates
    @FutureOrPresent(message = "dateOfJoining must be today or in the future")
    private LocalDate dateOfJoining;

    @NotNull(message = "recruiterId is required")
    private Long recruiterId;

    // optional: add @NotNull if HR is always assigned
    private Long hrId;

    @Size(max = 2000, message = "comments must not exceed 2000 characters")
    private String comments;

    @NotNull(message = "status is required")
    private ApplicationStatus status;

    @Size(max = 2000, message = "interviewDetails must not exceed 2000 characters")
    private String interviewDetails;
}

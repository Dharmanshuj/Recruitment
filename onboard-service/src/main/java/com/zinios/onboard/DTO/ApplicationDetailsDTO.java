package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.ApplicationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationDetailsDTO {
    private Long candidateId;
    private String clientName;
    private LocalDate dateOfJoining;
    private Long recruiterId;
    private Long hrId;
    private String comments;
    private ApplicationStatus status;
    private String interviewDetails;
}

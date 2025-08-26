package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.CandidateDoc;
import com.zinios.onboard.Entity.CandidateProDetails;
import com.zinios.onboard.Entity.ENUM.Gender;
import com.zinios.onboard.Entity.ENUM.MaritalStatus;
import com.zinios.onboard.Entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CandidateResponseDTO {
    private Long id;

    private String name;
    private String email;
    private Long user;
    private Long recruiterId;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dob;
    private MaritalStatus maritalStatus;

    private String city, state, country, pinCode;
    private String createdBy, updatedBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private CandidateProDetailsDTO candidateProDetails;
    private CandidateDocDTO candidateDoc;
}

package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.CurrentStatus;
import com.zinios.onboard.Entity.ENUM.Gender;
import com.zinios.onboard.Entity.ENUM.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CandidateRequestDTO {

    @NotBlank(message = "Recruiter ID is required")
    private Long recruiterId;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    private MaritalStatus maritalStatus;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Pin code is required")
    private String pinCode;

    @NotBlank(message = "currentOrg is required")
    private String currentOrg;

    @NotBlank(message = "currCTC is required")
    private String currCTC;

    @NotBlank(message = "noticePeriod is required")
    private String noticePeriod;

    @PastOrPresent(message = "lastWD must be in the past or today")
    private LocalDate lastWD;

    @NotBlank(message = "designation is required")
    private String designation;

    @NotBlank(message = "expCTC is required")
    private String expCTC;

    @NotNull(message = "currStatus is required")
    private CurrentStatus currStatus;

    @NotBlank(message = "skills is required")
    private String skills;

    @NotBlank(message = "totalEXP is required")
    private String totalEXP;

    @Size(max = 4000, message = "projects must not exceed 4000 characters")
    private String projects;

    private String prevOrg;

    @Email(message = "prevOrgManagerEmail must be a valid email")
    private String prevOrgManagerEmail;
}

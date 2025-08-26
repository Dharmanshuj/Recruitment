package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.CurrentStatus;
import com.zinios.onboard.Entity.ENUM.Gender;
import com.zinios.onboard.Entity.ENUM.MaritalStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class CandidateUpdateDTO {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    private Gender gender;

    @Past(message = "DOB must be in the past")
    private LocalDate dob;

    private MaritalStatus maritalStatus;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private String currentOrg;
    private String currCTC;
    private String noticePeriod;

    @PastOrPresent(message = "lastWD must be in the past or today")
    private LocalDate lastWD;

    private String designation;
    private String expCTC;
    private CurrentStatus currStatus;
    private String skills;
    private String totalEXP;

    @Size(max = 4000, message = "projects must not exceed 4000 characters")
    private List<Map<String, Object>> projects;

    private Map<String, Object> prevOrg;
    private String prevOrgManagerEmail;
}

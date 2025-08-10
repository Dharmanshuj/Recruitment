package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.Gender;
import com.zinios.onboard.Entity.ENUM.MaritalStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CandidateDTO {
    private String name;
    private String email;
    private Long recruiterId;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dob;
    private MaritalStatus maritalStatus;
    private String city;
    private String state;
    private String country;
    private String pinCode;
}

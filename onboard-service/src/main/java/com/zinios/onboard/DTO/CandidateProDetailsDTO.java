package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.CurrentStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CandidateProDetailsDTO {
    private Long candidateId;
    private String currentOrg;
    private String currCTC;
    private String noticePeriod;
    private LocalDate lastWD;
    private String designation;
    private String expCTC;
    private CurrentStatus currStatus;
    private String skills;
    private String totalEXP;
    private String projects;
    private String prevOrg;
    private String prevOrgManagerEmail;
}

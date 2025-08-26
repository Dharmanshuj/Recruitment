package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.Candidate;
import com.zinios.onboard.Entity.ENUM.CurrentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CandidateProDetailsDTO {
    private Long id;
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
    private List<Map<String, Object>> projects; // JSON list
    private Map<String, Object> prevOrg; // JSON object
    private String prevOrgManagerEmail;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}

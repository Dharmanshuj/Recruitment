package com.zinios.onboard.Entity;

import com.zinios.onboard.Entity.ENUM.CurrentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class CandidateProDetails {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false, unique = true)
    private Candidate candidate;

    private String currentOrg;
    private String currCTC;
    private String noticePeriod;
    private LocalDate lastWD;
    private String designation;
    private String expCTC;

    @Enumerated(EnumType.STRING)
    private CurrentStatus currStatus;

    @Lob
    private String skills;

    private String totalEXP;

    @Column(columnDefinition = "jsonb")
    private String projects; // JSON list

    @Column(columnDefinition = "jsonb")
    private String prevOrg; // JSON object

    private String prevOrgManagerEmail;

    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedTime;
}

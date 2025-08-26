package com.zinios.onboard.Entity;

import com.zinios.onboard.Entity.ENUM.Gender;
import com.zinios.onboard.Entity.ENUM.MaritalStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Candidate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiterId;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "is_active")
    private boolean isActive = true;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private String city, state, country, pinCode;
    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedTime;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private CandidateProDetails candidateProDetails;

    @OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private CandidateDoc candidateDoc;


}


package com.zinios.onboard.Entity;

import com.zinios.onboard.Entity.ENUM.InviteStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String candidateEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiterId;

    private String inviteLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus inviteStatus;

    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedTime;
}


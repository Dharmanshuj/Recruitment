package com.zinios.onboard.Repository;

import com.zinios.onboard.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findAllByIsActiveTrue();
    Optional<Candidate> findByEmailAndIsActiveTrue(String email);
    Optional<Candidate> findByUserIdAndUserIsActiveTrue(Long userId);
    List<Candidate> findAllByRecruiterId_IdAndIsActiveTrue(Long recruiterId);


    @Query("SELECT COUNT(c) FROM Candidate c " +
            "WHERE c.recruiterId.id = :recruiterId " +
            "AND (:startDate IS NULL OR c.createdTime >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdTime <= :endDate)")
    Long countCandidatesByRecruiterAndDate(
            @Param("recruiterId") Long recruiterId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}

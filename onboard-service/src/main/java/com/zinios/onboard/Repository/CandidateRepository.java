package com.zinios.onboard.Repository;

import com.zinios.onboard.Entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findAllByIsActiveTrue();
    Optional<Candidate> findByEmailAndIsActiveTrue(String email);
    Optional<Candidate> findByUserIdAndUserIsActiveTrue(Long userId);
    List<Candidate> findAllByRecruiterId_IdAndIsActiveTrue(Long recruiterId);
}

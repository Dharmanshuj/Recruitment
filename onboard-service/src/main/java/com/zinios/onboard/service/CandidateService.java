package com.zinios.onboard.service;

import com.zinios.onboard.DTO.CandidateDocDTO;
import com.zinios.onboard.DTO.CandidateRequestDTO;
import com.zinios.onboard.DTO.CandidateResponseDTO;
import com.zinios.onboard.DTO.CandidateUpdateDTO;
import com.zinios.onboard.Entity.*;
import com.zinios.onboard.Mapper.UserMapper;
import com.zinios.onboard.Repository.CandidateDocRepository;
import com.zinios.onboard.Repository.CandidateProDetailsRepository;
import com.zinios.onboard.Repository.CandidateRepository;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateProDetailsRepository candidateProDetailsRepository;
    private final CandidateDocRepository candidateDocRepository;
    private final UserMapper mapper;

    @Transactional
    public CandidateResponseDTO savePersonalDetails(Long userId, CandidateRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ZiniosException("No User found", HttpStatus.BAD_REQUEST));
        if(user.getUserType() != UserType.CANDIDATE) {
            throw new ZiniosException("Not a candidate", HttpStatus.BAD_REQUEST);
        }
        User recruiter = userRepository.findById(user.getReferenceId())
                .orElseThrow(() -> new ZiniosException("Recruiter not found", HttpStatus.BAD_REQUEST));
        Candidate candidate = mapper.toCandidate(recruiter, dto);
        candidate.setUser(user);
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        CandidateProDetails candidateProDetails = mapper.toProDetails(dto, candidate);
        candidate.setCandidateProDetails(candidateProDetails);
        user = userRepository.save(user);
        candidate = candidateRepository.save(candidate);
        candidateProDetails = candidateProDetailsRepository.save(candidateProDetails);
        return mapper.toResponseDTO(candidate);
    }

    @Transactional
    public CandidateResponseDTO updatePersonalDetails(Long userId, CandidateUpdateDTO dto) {
        Candidate candidate = candidateRepository.findByUserIdAndUserIsActiveTrue(userId)
                .orElseThrow(() -> new ZiniosException("Candidate not found", HttpStatus.BAD_REQUEST));
        mapper.updateCandidate(candidate, dto);
        candidate = candidateRepository.save(candidate);
        return mapper.toResponseDTO(candidate);
    }

    @Transactional
    public CandidateResponseDTO saveDocuments(Long userId, CandidateDocDTO dto) {
        Candidate candidate = candidateRepository.findByUserIdAndUserIsActiveTrue(userId)
                .orElseThrow(() -> new ZiniosException("Candidate not found", HttpStatus.BAD_REQUEST));
        CandidateDoc doc = mapper.toDocEntity(dto, candidate);
        candidate.setCandidateDoc(doc);
        doc = candidateDocRepository.save(doc);
        candidate = candidateRepository.save(candidate);
        return mapper.toResponseDTO(candidate);
    }

    @Transactional
    public CandidateResponseDTO updateDocuments(Long userId, CandidateDocDTO dto) {
        Candidate candidate = candidateRepository.findByUserIdAndUserIsActiveTrue(userId)
                .orElseThrow(() -> new ZiniosException("Candidate not found", HttpStatus.BAD_REQUEST));
        CandidateDoc doc = candidate.getCandidateDoc();
        mapper.updateDocEntity(doc, dto);
        doc = candidateDocRepository.save(doc);
        candidate = candidateRepository.save(candidate);
        return mapper.toResponseDTO(candidate);
    }

    public CandidateResponseDTO getDetails(Long userId) {
        Candidate candidate = candidateRepository.findByUserIdAndUserIsActiveTrue(userId)
                .orElseThrow(() -> new ZiniosException("No Candidate found", HttpStatus.BAD_REQUEST));

        return mapper.toResponseDTO(candidate);
    }

    public List<CandidateResponseDTO> getAllCandidates() {
        List<Candidate> candidates = candidateRepository.findAllByIsActiveTrue();
        return candidates.stream().map(mapper::toResponseDTO).toList();
    }

    public List<CandidateResponseDTO> getCandidatesById(Long id) {
        List<Candidate> candidates = candidateRepository.findAllByRecruiterId_IdAndIsActiveTrue(id);
        return candidates.stream().map(mapper::toResponseDTO).toList();
    }

    @Transactional
    public void deleteCandidate(String email) {
        Candidate candidate = candidateRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new ZiniosException("No Candidate found", HttpStatus.BAD_REQUEST));
        candidate.setActive(false);
        candidateRepository.save(candidate);
    }

    @Transactional
    public Long countCandidates(Long id, LocalDateTime start, LocalDateTime end) {
        return candidateRepository.countCandidatesByRecruiterAndDate(id, start, end);
    }
}

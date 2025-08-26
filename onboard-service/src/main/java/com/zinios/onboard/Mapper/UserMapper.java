package com.zinios.onboard.Mapper;

import com.zinios.onboard.DTO.*;
import com.zinios.onboard.Entity.*;
import com.zinios.onboard.Repository.CandidateProDetailsRepository;
import com.zinios.onboard.Repository.CandidateRepository;
import com.zinios.onboard.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateProDetailsRepository candidateProDetailsRepository;

    public User requestToUser(UserRequest userRequest, String createdBy) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Encode password
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmployeeId(userRequest.getEmployeeId());
        user.setUserType(userRequest.getUserType());
        user.setActive(true);
        user.setCreatedBy(createdBy);
        user.setUpdatedBy(createdBy);

        return user;
    }
    public User inviteToUser(Invite invite, String password) {
        User user = new User();
        User recruiter = invite.getRecruiterId();
        user.setName("Candidate" + UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(invite.getCandidateEmail());
        user.setReferenceId(recruiter.getId());
        user.setUserType(UserType.CANDIDATE);
        user.setActive(true);
        user.setCreatedBy(recruiter.getName());
        user.setUpdatedBy(recruiter.getName());
        return user;
    }

    @Transactional
    public Candidate toCandidate(User recruiter, CandidateRequestDTO dto) {
        // Build Candidate
        Candidate candidate = new Candidate();
        candidate.setRecruiterId(recruiter); // or recruiter.getId() if field is Long

        // Personal info
        candidate.setName(dto.getName());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setGender(dto.getGender());
        candidate.setDob(dto.getDob());
        candidate.setMaritalStatus(dto.getMaritalStatus());

        // Address
        candidate.setCity(dto.getCity());
        candidate.setState(dto.getState());
        candidate.setCountry(dto.getCountry());
        candidate.setPinCode(dto.getPinCode());

        // Audit
        candidate.setCreatedBy(dto.getName());
        candidate.setUpdatedBy(dto.getName());
        return candidate;
    }

    public CandidateProDetails toProDetails(CandidateRequestDTO dto, Candidate candidate) {
        CandidateProDetails pro = new CandidateProDetails();
        pro.setCandidate(candidate); // FK (owning side)

        // Professional details
        pro.setCurrentOrg(dto.getCurrentOrg());
        pro.setCurrCTC(dto.getCurrCTC());
        pro.setNoticePeriod(dto.getNoticePeriod());
        pro.setLastWD(dto.getLastWD());
        pro.setDesignation(dto.getDesignation());
        pro.setExpCTC(dto.getExpCTC());
        pro.setCurrStatus(dto.getCurrStatus());
        pro.setSkills(dto.getSkills());
        pro.setTotalEXP(dto.getTotalEXP());
        pro.setProjects(dto.getProjects());
        pro.setPrevOrg(dto.getPrevOrg());
        pro.setPrevOrgManagerEmail(dto.getPrevOrgManagerEmail());

        return pro;
    }

    public void updateCandidate(Candidate candidate, CandidateUpdateDTO dto) {

        User user = candidate.getUser();

        // Personal
        if (dto.getName() != null) {
            candidate.setName(dto.getName());
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            candidate.setEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            candidate.setPhoneNumber(dto.getPhoneNumber());
            user.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getGender() != null) candidate.setGender(dto.getGender());
        if (dto.getDob() != null) candidate.setDob(dto.getDob());
        if (dto.getMaritalStatus() != null) candidate.setMaritalStatus(dto.getMaritalStatus());

        // Address
        if (dto.getCity() != null) candidate.setCity(dto.getCity());
        if (dto.getState() != null) candidate.setState(dto.getState());
        if (dto.getCountry() != null) candidate.setCountry(dto.getCountry());
        if (dto.getPinCode() != null) candidate.setPinCode(dto.getPinCode());

        CandidateProDetails candidateProDetails = candidate.getCandidateProDetails();

        // Professional (align with fields you exposed in CandidateUpdateDTO)
        if (dto.getCurrentOrg() != null) candidateProDetails.setCurrentOrg(dto.getCurrentOrg());
        if (dto.getCurrCTC() != null) candidateProDetails.setCurrCTC(dto.getCurrCTC());
        if (dto.getNoticePeriod() != null) candidateProDetails.setNoticePeriod(dto.getNoticePeriod());
        if (dto.getLastWD() != null) candidateProDetails.setLastWD(dto.getLastWD());
        if (dto.getDesignation() != null) candidateProDetails.setDesignation(dto.getDesignation());
        if (dto.getExpCTC() != null) candidateProDetails.setExpCTC(dto.getExpCTC());
        if (dto.getCurrStatus() != null) candidateProDetails.setCurrStatus(dto.getCurrStatus());
        if (dto.getSkills() != null) candidateProDetails.setSkills(dto.getSkills());
        if (dto.getTotalEXP() != null) candidateProDetails.setTotalEXP(dto.getTotalEXP());
        if (dto.getProjects() != null) candidateProDetails.setProjects(dto.getProjects());
        if (dto.getPrevOrg() != null) candidateProDetails.setPrevOrg(dto.getPrevOrg());
        if (dto.getPrevOrgManagerEmail() != null) candidateProDetails.setPrevOrgManagerEmail(dto.getPrevOrgManagerEmail());
    }

    public CandidateResponseDTO toResponseDTO(Candidate candidate) {
        if (candidate == null) return null;

        CandidateResponseDTO dto = new CandidateResponseDTO();
        dto.setId(safe(candidate::getId));
        dto.setName(safe(candidate::getName));
        dto.setEmail(safe(candidate::getEmail));
        dto.setPhoneNumber(safe(candidate::getPhoneNumber));      // if present
        dto.setGender(safe(candidate::getGender));                 // if present
        dto.setMaritalStatus(safe(candidate::getMaritalStatus));   // if present
        dto.setDob(safe(candidate::getDob));       // if present
        dto.setCreatedBy(safe(candidate::getCreatedBy));
        dto.setUpdatedBy(safe(candidate::getUpdatedBy));
        dto.setCreatedTime(safe(candidate::getCreatedTime));
        dto.setUpdatedTime(safe(candidate::getUpdatedTime));

        dto.setUser(candidate.getUser() != null ? candidate.getUser().getId() : null);
        dto.setRecruiterId(candidate.getRecruiterId() != null ? candidate.getRecruiterId().getId() : null);

        CandidateProDetails pro = safe(candidate::getCandidateProDetails);
        dto.setCandidateProDetails(pro != null ? toProDetailsDTO(pro) : null);

        CandidateDoc doc = safe(candidate::getCandidateDoc);
        dto.setCandidateDoc(doc != null ? toDocDTO(doc) : null);


        return dto;
    }

    public CandidateProDetailsDTO toProDetailsDTO(CandidateProDetails entity) {
        CandidateProDetailsDTO dto = new CandidateProDetailsDTO();
        dto.setId(entity.getId());
        dto.setCandidateId(entity.getCandidate() != null ? entity.getCandidate().getId() : null);

        dto.setCurrentOrg(entity.getCurrentOrg());
        dto.setCurrCTC(entity.getCurrCTC());
        dto.setNoticePeriod(entity.getNoticePeriod());
        dto.setLastWD(entity.getLastWD());
        dto.setDesignation(entity.getDesignation());
        dto.setExpCTC(entity.getExpCTC());
        dto.setCurrStatus(entity.getCurrStatus());
        dto.setSkills(entity.getSkills());
        dto.setTotalEXP(entity.getTotalEXP());
        dto.setProjects(entity.getProjects());
        dto.setPrevOrg(entity.getPrevOrg());
        dto.setPrevOrgManagerEmail(entity.getPrevOrgManagerEmail());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedTime(entity.getCreatedTime());
        dto.setUpdatedTime(entity.getUpdatedTime());

        return dto;
    }

    public CandidateDocDTO toDocDTO(CandidateDoc entity) {
        if (entity == null) return null;

        CandidateDocDTO dto = new CandidateDocDTO();
        dto.setCandidateId(entity.getCandidate() != null ? entity.getCandidate().getId() : null);
        dto.setUanNo(entity.getUanNo());
        dto.setPanNo(entity.getPanNo());
        // DTO uses capital 'A' -> Aadhaar
        dto.setAadhaar(entity.getAadhaar());
        return dto;
    }

    public CandidateDoc toDocEntity(CandidateDocDTO dto, Candidate owner) {
        if (dto == null) return null;

        CandidateDoc entity = new CandidateDoc();
        entity.setCandidate(owner);                 // set relation
        entity.setUanNo(dto.getUanNo());
        entity.setPanNo(dto.getPanNo());
        entity.setAadhaar(dto.getAadhaar());        // map case difference
        return entity;
    }

    public void updateDocEntity(CandidateDoc target, CandidateDocDTO dto) {
        if (target == null || dto == null) return;
        target.setUanNo(dto.getUanNo());
        target.setPanNo(dto.getPanNo());
        target.setAadhaar(dto.getAadhaar());
    }

    // ---------- tiny helper to avoid accidental NullPointerExceptions ----------
    private interface supplier<T> { T get(); }
    private static <T> T safe(supplier<T> s) {
        try { return s.get(); } catch (Exception ignored) { return null; }
    }
}

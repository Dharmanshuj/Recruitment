package com.zinios.onboard.Mapper;

import com.zinios.onboard.DTO.CandidateRequestDTO;
import com.zinios.onboard.DTO.CandidateUpdateDTO;
import com.zinios.onboard.DTO.UserRequest;
import com.zinios.onboard.Entity.Candidate;
import com.zinios.onboard.Entity.CandidateProDetails;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Entity.UserType;
import com.zinios.onboard.Repository.CandidateProDetailsRepository;
import com.zinios.onboard.Repository.CandidateRepository;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public User inviteToUser(String email, String password, String createdBy) {
        User user = new User();
        user.setName("Candidate" + UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setUserType(UserType.CANDIDATE);
        user.setActive(true);
        user.setCreatedBy(createdBy);
        user.setUpdatedBy(createdBy);
        return user;
    }

    public void toCandidate(User user, CandidateRequestDTO dto) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (dto == null) {
            throw new IllegalArgumentException("CandidateDTO cannot be null");
        }

        // Recruiter lookup (foreign key to User)
        User recruiter = userRepository.findById(dto.getRecruiterId())
                .orElseThrow(() -> new ZiniosException("Recruiter not found", HttpStatus.BAD_REQUEST));

        Candidate candidate = new Candidate();
        CandidateProDetails candidateProDetails = candidate.getCandidateProDetails();

        // Link candidate -> user and recruiter
        candidate.setUser(user);
        candidate.setRecruiterId(recruiter); // assuming field type is User

        // Personal info
        candidate.setName(dto.getName());
        candidate.setEmail(dto.getEmail());
        candidate.setPhoneNumber(dto.getPhoneNumber());
        candidate.setGender(dto.getGender());
        candidate.setDob(dto.getDob());
        candidate.setMaritalStatus(dto.getMaritalStatus());

        // Address
        candidate.setCity(dto.getCity());
        candidate.setState(dto.getState());
        candidate.setCountry(dto.getCountry());
        candidate.setPinCode(dto.getPinCode());

        // Professional details (new in DTO)
        candidateProDetails.setCurrentOrg(dto.getCurrentOrg());
        candidateProDetails.setCurrCTC(dto.getCurrCTC());
        candidateProDetails.setNoticePeriod(dto.getNoticePeriod());
        candidateProDetails.setLastWD(dto.getLastWD());
        candidateProDetails.setDesignation(dto.getDesignation());
        candidateProDetails.setExpCTC(dto.getExpCTC());
        candidateProDetails.setCurrStatus(dto.getCurrStatus());
        candidateProDetails.setSkills(dto.getSkills());
        candidateProDetails.setTotalEXP(dto.getTotalEXP());
        candidateProDetails.setProjects(dto.getProjects());
        candidateProDetails.setPrevOrg(dto.getPrevOrg());
        candidateProDetails.setPrevOrgManagerEmail(dto.getPrevOrgManagerEmail());

        // Audit
        candidate.setCreatedBy(dto.getName());
        candidate.setUpdatedBy(dto.getName());

        // Keep User table in sync (name/phone/email)
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        userRepository.save(user);

        candidateRepository.save(candidate);
        candidateProDetailsRepository.save(candidateProDetails);
    }

    public void updateCandidate(String email, CandidateUpdateDTO dto) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new ZiniosException("Candidate not found", HttpStatus.BAD_REQUEST));
        if (candidate == null || dto == null) return;

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

        userRepository.save(user);
    }

}

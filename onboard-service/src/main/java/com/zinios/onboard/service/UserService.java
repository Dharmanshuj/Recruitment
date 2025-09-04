package com.zinios.onboard.service;

import com.zinios.onboard.DTO.*;
import com.zinios.onboard.Entity.ENUM.DateFilter;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Entity.UserType;
import com.zinios.onboard.Mapper.UserMapper;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final CandidateService candidateService;


    @Transactional
    public User registerUser(UserRequest request, String createdBy) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ZiniosException("User with email " + request.getEmail() + "already exists.", HttpStatus.BAD_REQUEST);
        }

        request.setEmployeeId(null);
        request.setUserType(UserType.CANDIDATE);

        User user = mapper.requestToUser(request, createdBy);
        return userRepository.save(user);
    }

    @Transactional
    public User addUser(UserRequest request, String createdBy) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ZiniosException("User with email " + request.getEmail() + "already exists.", HttpStatus.BAD_REQUEST);
        }

        if(request.getUserType() != UserType.CANDIDATE) {
            if(request.getEmployeeId() == null || request.getEmployeeId().isBlank()) {
                throw new ZiniosException("\"Employee ID is compulsory for user type \" + request.getUserType()", HttpStatus.BAD_REQUEST);
            }

            if(userRepository.existsByEmployeeId(request.getEmployeeId())) {
                throw new ZiniosException("User with employee ID " + request.getEmployeeId() + " already exists.", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            request.setEmployeeId(null);
        }

        User user = mapper.requestToUser(request, createdBy);
        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ZiniosException("User not found with email: " + email, HttpStatus.BAD_REQUEST));
        if(!user.isActive()) {
            throw new ZiniosException("User account is inactive", HttpStatus.BAD_REQUEST);
        }
        System.out.println(request.oldPassword() + "\n" + user.getPassword());
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new ZiniosException("Incorrect old password", HttpStatus.BAD_REQUEST);
        }
        if(passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new ZiniosException("Password cannot be same as previous password", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ZiniosException("User not found with email: " + email, HttpStatus.BAD_REQUEST));
        if(!user.isActive()) {
            throw new ZiniosException("User account is inactive", HttpStatus.BAD_REQUEST);
        }
        user.setActive(false);
        if(user.getUserType() == UserType.CANDIDATE) {
            candidateService.deleteCandidate(email);
        }
        userRepository.save(user);
    }

    public List<UserResponse> getAllRecruiters() {
        List<User> recruiters = userRepository.findAllByUserTypeAndIsActiveTrue(UserType.RECRUITER);
        return recruiters.stream().map(mapper::toUserResponse).toList();
    }

    public RecruiterStatsResponse getRecruiterStats(Long recruiterId, DateFilter filter,
                                                    LocalDateTime customStart, LocalDateTime customEnd) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = LocalDateTime.now();

        switch (filter) {
            case THIS_WEEK -> startDate = LocalDateTime.now().with(java.time.DayOfWeek.MONDAY).toLocalDate().atStartOfDay();
            case THIS_MONTH -> startDate = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();
            case THIS_YEAR -> startDate = LocalDateTime.now().withDayOfYear(1).toLocalDate().atStartOfDay();
            case CUSTOM -> {
                startDate = customStart;
                endDate = customEnd;
            }
            case ALL -> {
                startDate = null;
                endDate = null;
            }
        }

        Long total = candidateService.countCandidates(recruiterId, startDate, endDate);
        return new RecruiterStatsResponse(recruiterId, total);
    }
}

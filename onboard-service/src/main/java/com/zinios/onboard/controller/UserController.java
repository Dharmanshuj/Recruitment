package com.zinios.onboard.controller;

import com.zinios.onboard.DTO.*;
import com.zinios.onboard.Entity.ENUM.DateFilter;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.service.CandidateService;
import com.zinios.onboard.service.InviteService;
import com.zinios.onboard.service.JwtService;
import com.zinios.onboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final InviteService inviteService;
    private final CandidateService candidateService;
    private final JwtService jwtService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout Successfully");
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @Operation(summary = "Add", description = "Add a new user")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequest userRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String createdBy = authentication.getName();

        User newUser = userService.addUser(userRequest, createdBy);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/changePassword")
    @Operation(summary = "Change Password", description =  " Change the password")
    public ResponseEntity<String> changePass(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        userService.changePassword(email, changePasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/sendInvite")
    @PreAuthorize("hasAuthority('RECRUITER')")
    @Operation(summary = "Invite", description = "Send Invitation to recruit")
    public ResponseEntity<String> sendInvite(@RequestBody @Valid InviteRequestDTO request) {
        inviteService.sendInvite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Invite send successfully");
    }

    @GetMapping("/getAllCandidate")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'HR')")
    public ResponseEntity<List<CandidateResponseDTO>> getCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'HR')")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("/getCandidates")
    @PreAuthorize("hasAuthority('RECRUITER')")
    public ResponseEntity<List<CandidateResponseDTO>> getCandidates(@RequestHeader("Authorization") String auth) {
        Long id = jwtService.getUserIdFromAuthHeader(auth);
        return ResponseEntity.ok(candidateService.getCandidatesById(id));
    }

    @DeleteMapping("/deleteCandidate")
    @PreAuthorize("hasAuthority('RECRUITER')")
    public ResponseEntity<String> deleteCandidate(@RequestParam String email) {
        candidateService.deleteCandidate(email);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("/getRecruiters")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'HR')")
    public ResponseEntity<List<UserResponse>> getRecruiters() {
        return ResponseEntity.ok(userService.getAllRecruiters());
    }

    @GetMapping("/{recruiterId}/stats")
    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN', 'HR')")
    public ResponseEntity<RecruiterStatsResponse> getRecruiterStats(
            @PathVariable Long recruiterId,
            @RequestParam DateFilter filter,
            @Parameter(
                    description = "Start date (yyyy-MM-dd'T'HH:mm:ss) - required only if filter=CUSTOM",
                    example = "2025-09-01T00:00:00"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @Parameter(
                    description = "Start date (yyyy-MM-dd'T'HH:mm:ss) - required only if filter=CUSTOM",
                    example = "2025-09-01T00:00:00"
            )
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate
    ) {
        RecruiterStatsResponse stats = userService.getRecruiterStats(recruiterId, filter, startDate, endDate);
        return ResponseEntity.ok(stats);
    }
}

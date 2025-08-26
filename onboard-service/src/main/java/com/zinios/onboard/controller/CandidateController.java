package com.zinios.onboard.controller;

import com.zinios.onboard.DTO.CandidateDocDTO;
import com.zinios.onboard.DTO.CandidateRequestDTO;
import com.zinios.onboard.DTO.CandidateResponseDTO;
import com.zinios.onboard.DTO.CandidateUpdateDTO;
import com.zinios.onboard.service.CandidateService;
import com.zinios.onboard.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('CANDIDATE')")
@RequestMapping("/api/candidate")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService service;
    private final JwtService jwtService;

    @PostMapping("/saveDetails")
    public ResponseEntity<CandidateResponseDTO> savePersonalDetails(@RequestHeader("Authorization") String auth, @Valid @RequestBody CandidateRequestDTO dto) {
        Long userId = jwtService.getUserIdFromAuthHeader(auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.savePersonalDetails(userId, dto));
    }

    @PutMapping("/updateDetails")
    public ResponseEntity<CandidateResponseDTO> updatePersonalDetails(@RequestHeader("Authorization") String auth, @Valid @RequestBody CandidateUpdateDTO dto) {
        Long userId = jwtService.getUserIdFromAuthHeader(auth);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updatePersonalDetails(userId, dto));
    }

    @PostMapping("/uploadDoc")
    public ResponseEntity<CandidateResponseDTO> uploadDocuments(@RequestHeader("Authorization") String auth, @Valid @RequestBody CandidateDocDTO dto) {
        Long userId = jwtService.getUserIdFromAuthHeader(auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveDocuments(userId, dto));
    }

    @PutMapping("/updateDoc")
    public ResponseEntity<CandidateResponseDTO> updateDocuments(@RequestHeader("Authorization") String auth, @Valid @RequestBody CandidateDocDTO dto) {
        Long userId = jwtService.getUserIdFromAuthHeader(auth);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateDocuments(userId, dto));
    }

    @GetMapping("/get")
    public ResponseEntity<CandidateResponseDTO> getDetails(@RequestHeader("Authorization") String auth) {
        Long userId = jwtService.getUserIdFromAuthHeader(auth);
        return ResponseEntity.ok(service.getDetails(userId));
    }
}

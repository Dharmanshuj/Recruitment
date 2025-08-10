package com.zinios.onboard.service;

import com.zinios.onboard.DTO.InviteRequestDTO;
import com.zinios.onboard.DTO.UserRequest;
import com.zinios.onboard.Entity.ENUM.InviteStatus;
import com.zinios.onboard.Entity.Invite;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Entity.UserType;
import com.zinios.onboard.Mapper.UserFromInvite;
import com.zinios.onboard.Mapper.UserFromUserRequest;
import com.zinios.onboard.Repository.CandidateRepository;
import com.zinios.onboard.Repository.InviteRepository;
import com.zinios.onboard.Repository.UserRepository;
import com.zinios.onboard.exception.ZiniosException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InviteService {
    private final InviteRepository inviteRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final UserFromInvite mapper;

    public static final String inviteLink = "http://localhost:8082/api/auth/respond";
    public void sendInvite(InviteRequestDTO request) {
        User recruiter = userRepository.findById(request.getRecruiterId())
                .orElseThrow(() -> new ZiniosException("No recruiter found", HttpStatus.BAD_REQUEST));
        if(recruiter.getUserType() != UserType.RECRUITER) {
            throw new ZiniosException("Not a recruiter", HttpStatus.BAD_REQUEST);
        }

        Invite invite = Invite.builder()
                .candidateEmail(request.getCandidateEmail())
                .recruiterId(recruiter)
                .inviteStatus(InviteStatus.SENT)
                .createdTime(LocalDateTime.now())
                .build();

        invite = inviteRepository.save(invite);

        String acceptLink = inviteLink + "/" + invite.getId() + "/ACCEPTED";
        String rejectLink = inviteLink + "/" + invite.getId() + "/REJECTED";
        invite.setInviteLink(acceptLink);
        inviteRepository.save(invite);
        String subject = "Interview Invitation from ZiniosEdge";
        String body;
        try(var is = new ClassPathResource("EmailBody.html").getInputStream()) {
//            String template = Files.readString(Paths.get("src/main/resources/EmailBody.html"), StandardCharsets.UTF_8);
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8)
                    .replace("${CANDIDATE_NAME}", escape(request.getCandidateEmail()))
                    .replace("${ACCEPT_LINK}", acceptLink)
                    .replace("${REJECT_LINK}", rejectLink);
        }
        catch (IOException e) {
            throw new ZiniosException("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        emailService.sendEmail(request.getCandidateEmail(), subject, body);
    }

    public boolean updateStatus(Long inviteId, String status, String password) {
        Optional<Invite> optionalInvite = inviteRepository.findById(inviteId);
        if(optionalInvite.isEmpty()) return false;

        Invite invite = optionalInvite.get();

        try {
            InviteStatus inviteStatus = InviteStatus.valueOf(status.toUpperCase().trim());
            invite.setInviteStatus(inviteStatus);
            invite.setUpdatedTime(LocalDateTime.now());
            inviteRepository.save(invite);
            if(inviteStatus == InviteStatus.ACCEPTED) {
                User user = mapper.inviteToUser(invite.getCandidateEmail(),password, invite.getRecruiterId().getName());
                userRepository.save(user);
            }
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}

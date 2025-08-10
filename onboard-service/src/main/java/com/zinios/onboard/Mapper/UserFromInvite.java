package com.zinios.onboard.Mapper;

import com.zinios.onboard.Entity.User;
import com.zinios.onboard.Entity.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFromInvite {
    private final PasswordEncoder passwordEncoder;

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
}

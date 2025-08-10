package com.zinios.onboard.Mapper;

import com.zinios.onboard.DTO.UserRequest;
import com.zinios.onboard.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFromUserRequest {
    private final PasswordEncoder passwordEncoder;

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
}

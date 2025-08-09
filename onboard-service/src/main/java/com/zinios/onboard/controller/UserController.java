package com.zinios.onboard.controller;

import com.zinios.onboard.DTO.ChangePasswordRequest;
import com.zinios.onboard.DTO.UserRequest;
import com.zinios.onboard.Entity.User;
import com.zinios.onboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
}

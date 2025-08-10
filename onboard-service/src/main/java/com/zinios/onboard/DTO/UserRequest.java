package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be black")
    private String password;

    private String phoneNumber;

    private String employeeId;

    @NotNull(message = "User Type cannot be null")
    private UserType userType;

    private String createdBy;
}

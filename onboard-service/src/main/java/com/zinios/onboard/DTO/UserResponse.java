package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.UserType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    Long id;
    private String name;
    private String email;
    private Long referenceId;
    private String phoneNumber;
    private String employeeId;
    private UserType userType;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime updatedTime;
}

package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.User;
import lombok.Data;

@Data
public class InviteRequestDTO {
    private String candidateEmail;
    private Long recruiterId;
}


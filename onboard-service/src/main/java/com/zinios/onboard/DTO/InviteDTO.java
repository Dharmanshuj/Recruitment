package com.zinios.onboard.DTO;

import com.zinios.onboard.Entity.ENUM.InviteStatus;
import lombok.Data;

@Data
public class InviteDTO {
    private String candidateEmail;
    private Long recruiterId;
    private String inviteLink;
    private InviteStatus inviteStatus;
}


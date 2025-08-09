package com.zinios.onboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String name;
    private String email;
    private String userType;
    private Boolean isActive;
}

package com.zinios.onboard.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CandidateDocDTO {
    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotBlank(message = "UAN number is required")
    @Pattern(regexp = "\\d{12}", message = "UAN number must be exactly 12 digits")
    private String uanNo;

    @NotBlank(message = "PAN number is required")
    @Pattern(regexp = "[A-Z]{5}\\d{4}[A-Z]{1}", message = "Invalid PAN number format")
    private String panNo;

    @NotBlank(message = "Aadhaar number is required")
    @Pattern(regexp = "\\d{12}", message = "Aadhaar number must be exactly 12 digits")
    private String Aadhaar;
}

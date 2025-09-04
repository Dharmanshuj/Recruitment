package com.zinios.onboard.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecruiterStatsResponse {
    private Long recruiterId;
    private Long totalCandidates;
}

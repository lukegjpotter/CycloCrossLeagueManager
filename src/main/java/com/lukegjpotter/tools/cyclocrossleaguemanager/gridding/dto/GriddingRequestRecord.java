package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GriddingRequestRecord(
        @NotBlank(message = "signups must be supplied")
        String signups,

        @NotBlank(message = "gridding must be supplied")
        String gridding,

        @Min(value = 1, message = "roundNumber must be 1, or greater")
        int roundNumber
) {
}

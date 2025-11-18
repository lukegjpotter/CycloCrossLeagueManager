package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UpdateStandingsRequestRecord(
        @NotBlank(message = "roundResultsUrl must not be blank")
        String roundResultsUrl,

        @Min(value = 1, message = "roundNumber must be 1, or greater")
        int roundNumber
) {
}

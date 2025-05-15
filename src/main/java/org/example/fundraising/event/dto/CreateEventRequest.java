package org.example.fundraising.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateEventRequest(
        @NotBlank(message = "'eventName' - must be specified")
        @Size(
                max = 255,
                message = "'eventName' - can't exceed 255 characters."
        )
        String eventName,

        @NotNull(message = "'currencyCode' - must be specified")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "'currencyCode' - must match the ISO 4217 standard."
        )
        String currencyCode

){
}

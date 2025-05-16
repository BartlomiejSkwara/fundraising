package org.example.fundraising.collectionbox.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.bind.annotation.RequestParam;

public record AddCashToBoxRequest (
        @NotNull(message = "'currencyCode' - must be specified")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "'currencyCode' - must match the ISO 4217 standard."
        )
        String currencyCode,
        @Pattern(
                regexp = "^\\d{1,11}.\\d{2}$",
                message = "'cashAmount' - must be formated in following format 5.00")
        @NotNull
        String cashAmount
){
}

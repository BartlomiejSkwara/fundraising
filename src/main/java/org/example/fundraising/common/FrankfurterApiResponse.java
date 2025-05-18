package org.example.fundraising.common;

import java.util.HashMap;

public record FrankfurterApiResponse(
        String base,
        HashMap<String,String> rates
) {
}

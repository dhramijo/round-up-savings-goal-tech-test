package com.starlingbank.roundup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyAndMinorUnits {
    private String currency;
    private long minorUnits;
}

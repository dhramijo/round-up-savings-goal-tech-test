package com.starlingbank.roundup.request;

import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AmountRequest {
  private CurrencyAndMinorUnits amount;
}

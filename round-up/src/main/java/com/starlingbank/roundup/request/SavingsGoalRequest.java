package com.starlingbank.roundup.request;

import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SavingsGoalRequest {
  private String accountUid;
  private String savingsGoalUid;
  private String transferUid;
  private CurrencyAndMinorUnits amount;
}

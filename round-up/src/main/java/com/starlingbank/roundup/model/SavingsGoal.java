package com.starlingbank.roundup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoal {
  private String savingsGoalUid;
  private String name;
  private CurrencyAndMinorUnits target;
  private CurrencyAndMinorUnits totalSaved;
  private String savedPercentage;
}

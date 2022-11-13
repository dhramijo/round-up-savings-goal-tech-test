package com.starlingbank.roundup.services;

import static com.starlingbank.roundup.utils.Constants.ACCOUNT_ID;
import static com.starlingbank.roundup.utils.Constants.SAVINGS_GOAL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import com.starlingbank.roundup.model.SavingsGoal;
import com.starlingbank.roundup.request.SavingsGoalRequest;
import com.starlingbank.roundup.restclient.StarlingClient;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SavingsGoalServiceTest {

  @MockBean private StarlingClient starlingClient;
  @Autowired private SavingsGoalService savingsGoalService;

  @Test
  void test_find_savings_goal() {
    var expectedSavingsGoal = (SavingsGoal) ResourceFileLoaderUtils.loadResourceFile(
        "json/SavingsGoal.json", SavingsGoal.class);
    when(starlingClient.getSavingsGoal(anyString(), any())).thenReturn(expectedSavingsGoal);

    var returnedSavingsGoal = savingsGoalService.findSavingsGoal(ACCOUNT_ID, SAVINGS_GOAL_ID);

    assertThat(expectedSavingsGoal).isEqualTo(returnedSavingsGoal);
    verify(starlingClient, times(1)).getSavingsGoal(isA(String.class), isA(String.class));

  }

  @Test
  void test_add_money_into_savings_goal() {
    var retrievedSavingsGoal = SavingsGoal.builder()
        .savingsGoalUid(SAVINGS_GOAL_ID)
        .name("Holidays")
        .target(CurrencyAndMinorUnits.builder()
            .currency("GBP")
            .minorUnits(10000l)
            .build())
        .totalSaved(CurrencyAndMinorUnits.builder()
            .currency("GBP")
            .minorUnits(5000l)
            .build())
        .savedPercentage("50")
        .build();

    doNothing().when(starlingClient).addMoneyIntoSavingsGoal(anyString(), any());

    savingsGoalService.addMoneyIntoSavingsGoal(ACCOUNT_ID,10000l, retrievedSavingsGoal);

    verify(starlingClient, times(1)).addMoneyIntoSavingsGoal(isA(String.class), isA(SavingsGoalRequest.class));

  }
}
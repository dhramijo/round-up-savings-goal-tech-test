package com.starlingbank.roundup.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.starlingbank.roundup.model.SavingsGoal;
import com.starlingbank.roundup.response.AccountResponse;
import com.starlingbank.roundup.response.TransactionResponse;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SavingsGoalAmountServiceTest {

  @MockBean
  private TransactionService transactionService;

  @MockBean
  private AccountService accountService;

  @MockBean
  private SavingsGoalService savingGoalService;

  @MockBean
  private RoundUpService roundUpService;

  @Autowired
  private SavingsGoalAmountService savingsGoalAmountService;

  public static final String SAVINGS_GOAL_ID = "d84b33b2-05cf-4a8f-9b52-6cf95e5ed790";

  @Test
  void test_update_savings_goal_amount() {
    var expectedAccountResponse = (AccountResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Accounts.json", AccountResponse.class);
    var expectedAccount = expectedAccountResponse.getAccounts().get(0);
    when(accountService.retrieveAccount()).thenReturn(expectedAccount);

    var expectedTransactionResponse = (TransactionResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Transactions.json", TransactionResponse.class);
    var expectedTransactions = expectedTransactionResponse.getFeedItems();
    when(transactionService.retrieveTransactions(any(), any(), anyString(), anyString())).thenReturn(expectedTransactions);

    var expectedRoundedUpAmount = 1792l;
    when(roundUpService.calculateSavingsGoalAmount(any())).thenReturn(expectedRoundedUpAmount);

    var expectedSavingsGoal = (SavingsGoal) ResourceFileLoaderUtils.loadResourceFile(
        "json/SavingsGoal.json", SavingsGoal.class);
    when(savingGoalService.findSavingsGoal(anyString(), any())).thenReturn(expectedSavingsGoal);

    doNothing().when(savingGoalService).addMoneyIntoSavingsGoal(anyString(), anyLong(), any());

    savingsGoalAmountService.updateSavingsGoalAmount(null, null, SAVINGS_GOAL_ID);

    verify(accountService, times(1)).retrieveAccount();
    verify(transactionService, times(1)).retrieveTransactions(isNull(), isNull(), isA(String.class), isA(String.class));
    verify(roundUpService, times(1)).calculateSavingsGoalAmount(isA(List.class));
    verify(savingGoalService, times(1)).findSavingsGoal(isA(String.class), isA(String.class));
    verify(savingGoalService, times(1)).addMoneyIntoSavingsGoal(isA(String.class), isA(Long.class), isA(SavingsGoal.class));
  }
}
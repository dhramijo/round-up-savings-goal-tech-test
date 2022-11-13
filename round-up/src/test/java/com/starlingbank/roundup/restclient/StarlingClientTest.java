package com.starlingbank.roundup.restclient;

import static com.starlingbank.roundup.utils.Constants.ACCOUNT_ID;
import static com.starlingbank.roundup.utils.Constants.CATEGORY_ID;
import static com.starlingbank.roundup.utils.Constants.SAVINGS_GOAL_ID;
import static com.starlingbank.roundup.utils.Constants.TRANSFER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import com.starlingbank.roundup.model.SavingsGoal;
import com.starlingbank.roundup.request.SavingsGoalRequest;
import com.starlingbank.roundup.response.AccountResponse;
import com.starlingbank.roundup.response.CreateOrUpdateSavingsGoalResponse;
import com.starlingbank.roundup.response.TransactionResponse;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class StarlingClientTest {
  public static final String starlingBasePath = "https://dummy-api-base-path-url";
  public static final String updateSavingsGoal = "dummy-update-savings-goal-endpoint";
  @MockBean private RestTemplate restTemplate;
  @Autowired private StarlingClient starlingClient;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(starlingClient, "starlingBasePath", "https://dummy-api-base-path-url");
    ReflectionTestUtils.setField(starlingClient, "updateSavingsGoal", "dummy-update-savings-goal-endpoint");
  }

  @Test
  void test_get_accounts() {
    var expectedAccountResponse = (AccountResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Accounts.json", AccountResponse.class);
    var expectedAccount = expectedAccountResponse.getAccounts().get(0);

    when(restTemplate.exchange(anyString(), any(), any(), eq(AccountResponse.class)))
        .thenReturn(new ResponseEntity<>(expectedAccountResponse, HttpStatus.OK));

    var returnedAccount = starlingClient.getAccount();

    assertEquals(expectedAccount, returnedAccount);
  }


  @Test
  void test_get_account_transactions_without_specified_week() {
    var expectedTransactionResponse = (TransactionResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Transactions.json", TransactionResponse.class);
    var expectedTransactions = expectedTransactionResponse.getFeedItems();

    when(restTemplate.exchange(anyString(), any(), any(), eq(TransactionResponse.class)))
        .thenReturn(new ResponseEntity<>(expectedTransactionResponse, HttpStatus.OK));

    var returnedTransactions = starlingClient.getAccountTransactions(null, null, ACCOUNT_ID, CATEGORY_ID);

    assertThat(expectedTransactions).isEqualTo(returnedTransactions);
  }


  @Test
  void test_get_account_transactions_with_specified_week() {
    var expectedTransactionResponse = (TransactionResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Transactions.json", TransactionResponse.class);
    var expectedTransactions = expectedTransactionResponse.getFeedItems();

    when(restTemplate.exchange(anyString(), any(), any(), eq(TransactionResponse.class)))
        .thenReturn(new ResponseEntity<>(expectedTransactionResponse, HttpStatus.OK));

    var dateFrom = "2022-11-04T15:35:22.801Z";
    var dateTo = "2022-11-11T15:35:22.802Z";
    var returnedTransactions = starlingClient.getAccountTransactions(dateFrom, dateTo, ACCOUNT_ID, CATEGORY_ID);

    assertThat(expectedTransactions).isEqualTo(returnedTransactions);
  }


  @Test
  void test_get_savings_goal() {
    var expectedSavingsGoal = (SavingsGoal) ResourceFileLoaderUtils.loadResourceFile(
        "json/SavingsGoal.json", SavingsGoal.class);

    when(restTemplate.exchange(anyString(), any(), any(), eq(SavingsGoal.class)))
        .thenReturn(new ResponseEntity<>(expectedSavingsGoal, HttpStatus.OK));

    var returnedSavingsGoal = starlingClient.getSavingsGoal(ACCOUNT_ID, SAVINGS_GOAL_ID);

    assertThat(expectedSavingsGoal).isEqualTo(returnedSavingsGoal);
  }


  @Test
  void test_add_money_to_savings_goal() {
    var savingsGoalRequest = SavingsGoalRequest.builder()
        .accountUid(ACCOUNT_ID)
        .savingsGoalUid(SAVINGS_GOAL_ID)
        .transferUid(TRANSFER_ID)
            .amount(CurrencyAndMinorUnits.builder()
                .currency("GBP")
                .minorUnits(10000l)
                .build())
                .build();

    when(restTemplate.exchange(anyString(), any(), any(), eq(CreateOrUpdateSavingsGoalResponse.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    starlingClient.addMoneyIntoSavingsGoal(ACCOUNT_ID, savingsGoalRequest);

    verify(restTemplate).exchange(
        eq(starlingBasePath + updateSavingsGoal + ACCOUNT_ID +
            "/savings-goals/" + SAVINGS_GOAL_ID + "/add-money/" + TRANSFER_ID),
        any(), any(), eq(CreateOrUpdateSavingsGoalResponse.class));
  }
}
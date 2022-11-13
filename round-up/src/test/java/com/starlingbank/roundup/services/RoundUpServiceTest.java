package com.starlingbank.roundup.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.starlingbank.roundup.response.TransactionResponse;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoundUpServiceTest {

  @Autowired private RoundUpService roundUpService;

  @Test
  void test_calculate_savings_goal_amount() {
    var expectedNewRoundUpAmount = 1792l;

    var transactionResponse = (TransactionResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Transactions.json", TransactionResponse.class);
    var transactions = transactionResponse.getFeedItems();

    var returnedRoundUpAmount = roundUpService.calculateSavingsGoalAmount(transactions);

    assertEquals(expectedNewRoundUpAmount, returnedRoundUpAmount);
  }
}
package com.starlingbank.roundup.services;

import static com.starlingbank.roundup.utils.Constants.ACCOUNT_ID;
import static com.starlingbank.roundup.utils.Constants.CATEGORY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.starlingbank.roundup.response.TransactionResponse;
import com.starlingbank.roundup.restclient.StarlingClient;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TransactionServiceTest {

  @MockBean private StarlingClient starlingClient;
  @Autowired private TransactionService transactionService;

  @Test
  void test_retrieve_transactions() {
    var expectedTransactionResponse = (TransactionResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Transactions.json", TransactionResponse.class);
    var expectedTransactions = expectedTransactionResponse.getFeedItems();
    when(starlingClient.getAccountTransactions(any(), any(),anyString(), anyString())).thenReturn(expectedTransactions);

    var returnedTransactions = transactionService.retrieveTransactions(null, null, ACCOUNT_ID, CATEGORY_ID);

    assertThat(expectedTransactions).isEqualTo(returnedTransactions);
  }
}
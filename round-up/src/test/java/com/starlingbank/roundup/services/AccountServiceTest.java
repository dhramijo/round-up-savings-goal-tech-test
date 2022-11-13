package com.starlingbank.roundup.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.starlingbank.roundup.response.AccountResponse;
import com.starlingbank.roundup.restclient.StarlingClient;
import com.starlingbank.roundup.utils.ResourceFileLoaderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AccountServiceTest {

  @MockBean private StarlingClient starlingClient;
  @Autowired private AccountService accountService;

  @Test
  void test_retrieve_account() {
    var expectedAccountResponse = (AccountResponse) ResourceFileLoaderUtils.loadResourceFile(
        "json/Accounts.json", AccountResponse.class);
    var expectedAccount = expectedAccountResponse.getAccounts().get(0);

    when(starlingClient.getAccount()).thenReturn(expectedAccount);

    var returnedAccount = accountService.retrieveAccount();

    assertEquals(expectedAccount, returnedAccount);
  }
}
package com.starlingbank.roundup.restclient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.starlingbank.roundup.enumeration.AccountType;
import com.starlingbank.roundup.exception.ResourceNotFoundException;
import com.starlingbank.roundup.model.Account;
import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import com.starlingbank.roundup.model.SavingsGoal;
import com.starlingbank.roundup.model.Transaction;
import com.starlingbank.roundup.request.AmountRequest;
import com.starlingbank.roundup.request.SavingsGoalRequest;
import com.starlingbank.roundup.response.AccountResponse;
import com.starlingbank.roundup.response.CreateOrUpdateSavingsGoalResponse;
import com.starlingbank.roundup.response.TransactionResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@RequiredArgsConstructor
public class StarlingClient {

  @Value("${api.token}")
  private String token;

  @Value("${starling.base-path}")
  private String starlingBasePath;

  @Value("${starling.endpoint.get-accounts}")
  private String getAccounts;

  @Value("${starling.endpoint.get-transactions}")
  private String getTransaction;

  @Value("${starling.endpoint.get-savings-goals}")
  private String getSavingsGoal;

  @Value("${starling.endpoint.update-savings-goals}")
  private String updateSavingsGoal;
  private final RestTemplate restTemplate;

  public List<Transaction> getAccountTransactions(final String dateFrom, final String dateTo, final String accountId, final String categoryId) {
    log.info("Get Account Transactions - Account Id: {}", accountId);

    final var url = starlingBasePath + getTransaction + "/"
        + accountId + "/category/" + categoryId + "/transactions-between";

    final var headers = getHeaders();
    final var requestEntity = new HttpEntity<>(headers);

    final var urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("minTransactionTimestamp", dateFrom)
        .queryParam("maxTransactionTimestamp", dateTo)
        .encode()
        .toUriString();

    final var response = restTemplate.exchange(
        urlTemplate,
        HttpMethod.GET,
        requestEntity,
        TransactionResponse.class
    );

    if (response.getBody() != null)
      return Objects.requireNonNull(response.getBody().getFeedItems());
    else
      throw new ResourceNotFoundException("Transaction Response Not Found");
  }

  public Account getAccount() {

    final var url = starlingBasePath + getAccounts;
    final var headers = getHeaders();
    final var requestEntity = new HttpEntity<>(headers);

    final var response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        AccountResponse.class
    );

    if (response.getBody() != null) {
      final var accounts = Objects.requireNonNull(response.getBody().getAccounts());
      return Optional.ofNullable(accounts).get()
          .stream()
          .filter(account -> AccountType.PRIMARY.name().equals(account.getAccountType())
              && ("GBP".equals(account.getCurrency())))
          .findFirst()
          .orElseThrow(() -> new ResourceNotFoundException("Account Not found"));
    } else {
      throw new ResourceNotFoundException("Account Response Not Found");
    }
  }

  public SavingsGoal getSavingsGoal(final String accountId, final String savingsGoalId) {
    log.info("Get Savings Goal - Account Id: {}", accountId);

    final var url = starlingBasePath + getSavingsGoal + accountId + "/savings-goals/" + savingsGoalId;
    final var headers = getHeaders();
    final var requestEntity = new HttpEntity<>(headers);

    return restTemplate.exchange(
        url,
        HttpMethod.GET,
        requestEntity,
        SavingsGoal.class
    ).getBody();

  }

  public void addMoneyIntoSavingsGoal(final String accountId, final SavingsGoalRequest savingsGoalRequest) {
    log.info("Add Money Into Savings Goal - Account Id: {}", accountId);

    final var url = starlingBasePath + updateSavingsGoal + accountId +
        "/savings-goals/" + savingsGoalRequest.getSavingsGoalUid() +
        "/add-money/" + savingsGoalRequest.getTransferUid();

    final var headers = getHeaders();

    final var amountRequest = AmountRequest.builder()
        .amount(CurrencyAndMinorUnits.builder()
            .minorUnits(savingsGoalRequest.getAmount().getMinorUnits())
            .currency(savingsGoalRequest.getAmount().getCurrency())
            .build()).build();

    final var requestEntity = new HttpEntity<>(amountRequest, headers);

    restTemplate.exchange(
        url,
        HttpMethod.PUT,
        requestEntity,
        CreateOrUpdateSavingsGoalResponse.class
    );
  }

  public HttpHeaders getHeaders() {
    final var headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + token);
    return headers;
  }
}

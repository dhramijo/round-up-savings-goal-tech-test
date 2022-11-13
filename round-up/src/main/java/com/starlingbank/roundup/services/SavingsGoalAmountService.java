package com.starlingbank.roundup.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavingsGoalAmountService {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final SavingsGoalService savingGoalService;
  private final RoundUpService roundUpService;

  public void updateSavingsGoalAmount(final String dateFrom, final String dateTo, final String savingsGoalId) {
    final var account = accountService.retrieveAccount();
    log.info("Account retrieved successfully.");
    final var accountUid = account.getAccountUid();
    final var transactions = transactionService.retrieveTransactions(dateFrom, dateTo, accountUid, account.getDefaultCategory());
    log.info("Transactions retrieved successfully.");
    final var roundedUpAmount = roundUpService.calculateSavingsGoalAmount(transactions);
    log.info("RoundedUp Amount calculated successfully.");
    final var retrievedSavingsGoal  = savingGoalService.findSavingsGoal(accountUid, savingsGoalId);
    log.info("Savings Goal retrieved successfully.");
    savingGoalService.addMoneyIntoSavingsGoal(accountUid, roundedUpAmount, retrievedSavingsGoal);
    log.info("Savings Goal Total Saved Amount updated successfully.");
  }
}

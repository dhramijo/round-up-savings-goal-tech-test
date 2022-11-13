package com.starlingbank.roundup.services;

import static com.starlingbank.roundup.util.TransactionDataHelper.getStartEndDatePair;

import com.starlingbank.roundup.model.Transaction;
import com.starlingbank.roundup.restclient.StarlingClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final StarlingClient starlingClient;

    public List<Transaction> retrieveTransactions(final String dateFrom, final String dateTo, final String accountId, final String categoryId) {
        final var minTransactionTimestamp = getStartEndDatePair(dateFrom, dateTo).getLeft();
        final var maxTransactionTimestamp = getStartEndDatePair(dateFrom, dateTo).getRight();
        return starlingClient.getAccountTransactions(minTransactionTimestamp, maxTransactionTimestamp, accountId, categoryId);
    }



}

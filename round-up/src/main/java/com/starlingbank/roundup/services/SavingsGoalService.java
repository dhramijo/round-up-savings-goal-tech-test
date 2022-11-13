package com.starlingbank.roundup.services;

import com.starlingbank.roundup.model.CurrencyAndMinorUnits;
import com.starlingbank.roundup.model.SavingsGoal;
import com.starlingbank.roundup.request.SavingsGoalRequest;
import com.starlingbank.roundup.restclient.StarlingClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SavingsGoalService {
    private final StarlingClient starlingClient;

    public SavingsGoal findSavingsGoal(final String accountId, final String savingsGoalId) {
        return starlingClient.getSavingsGoal(accountId, savingsGoalId);
    }

    public void addMoneyIntoSavingsGoal(final String accountUid, final long roundedUpAmount, final SavingsGoal retrievedSavingsGoal) {
        final var savingsGoalAddMoneyRequest = SavingsGoalRequest.builder()
            .accountUid(accountUid)
            .savingsGoalUid(retrievedSavingsGoal.getSavingsGoalUid())
            .transferUid(UUID.randomUUID().toString())
            .amount(CurrencyAndMinorUnits.builder()
                .currency(retrievedSavingsGoal.getTotalSaved().getCurrency())
                .minorUnits(roundedUpAmount)
                .build()).build();

        starlingClient.addMoneyIntoSavingsGoal(accountUid, savingsGoalAddMoneyRequest);
    }
}

package com.starlingbank.roundup.services;

import com.starlingbank.roundup.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import javax.money.Monetary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.MonetaryQueries;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoundUpService {

    public long calculateSavingsGoalAmount(final List<Transaction> transactions) {
      final var currencyUnit = Monetary.getCurrency(Locale.UK);
      var totalRoundedUpAmount = new BigDecimal(0);
      for (var transaction : transactions) {
        totalRoundedUpAmount = totalRoundedUpAmount
            .add(calculateRoundupAmount(Money.ofMinor(currencyUnit, transaction.getAmount().getMinorUnits()).getNumberStripped()));
      }
      totalRoundedUpAmount = totalRoundedUpAmount.setScale(2, RoundingMode.HALF_EVEN);

      // The total rounded up value has to be re-converted into minorUnit as expected by the Savings Goal request
      return Money.of(totalRoundedUpAmount, currencyUnit).query(MonetaryQueries.convertMinorPart());
    }

    private static BigDecimal calculateRoundupAmount(final BigDecimal amountToRound) {
      final var amount = amountToRound;
      return amount.setScale(0, RoundingMode.UP).subtract(amount);
    }
}
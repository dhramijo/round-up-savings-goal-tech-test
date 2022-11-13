package com.starlingbank.roundup.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.apache.commons.lang3.tuple.Pair;

public class TransactionDataHelper {

  private TransactionDataHelper() {
    throw new IllegalStateException("Utility class");
  }

  public static Pair<String, String> getStartEndDatePair(String dateFrom, String dataTo) {
    var inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    var calculatedDateFrom = isBlankString(dateFrom) ? LocalDateTime.now().minusDays(7).format(inputFormatter) : dateFrom;
    var calculatedDateTo = isBlankString(dataTo) ? LocalDateTime.now().format(inputFormatter) : dataTo;
    return Pair.of(calculatedDateFrom, calculatedDateTo);
  }

  private static boolean isBlankString(String string) {
    return string == null || string.isBlank();
  }

}

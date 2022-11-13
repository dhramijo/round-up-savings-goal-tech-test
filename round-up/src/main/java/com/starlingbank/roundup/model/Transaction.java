package com.starlingbank.roundup.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
  private String feedItemUid;
  private String categoryUid;
  private CurrencyAndMinorUnits amount;
  private CurrencyAndMinorUnits sourceAmount;
  private String direction;
  private String updatedAt;
  private String transactionTime;
  private String settlementTime;
  private String source;
  private String status;
  private String transactingApplicationUserUid;
  private String counterPartyType;
  private String counterPartyUid;
  private String counterPartyName;
  private String counterPartySubEntityUid;
  private String counterPartySubEntityName;
  private String counterPartySubEntityIdentifier;
  private String counterPartySubEntitySubIdentifier;
  private String reference;
  private String country;
  private String spendingCategory;
  private boolean hasAttachment;
  private boolean hasReceipt;
}

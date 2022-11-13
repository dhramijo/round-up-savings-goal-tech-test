package com.starlingbank.roundup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  private String accountUid;
  private String accountType;
  private String defaultCategory;
  private String currency;
  private String createdAt;
  private String name;
}

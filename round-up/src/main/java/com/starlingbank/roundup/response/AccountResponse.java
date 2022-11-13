package com.starlingbank.roundup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.starlingbank.roundup.model.Account;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountResponse {
    private List<Account> accounts;
}

package com.starlingbank.roundup.services;

import com.starlingbank.roundup.model.Account;
import com.starlingbank.roundup.restclient.StarlingClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final StarlingClient starlingClient;
    public Account retrieveAccount() {
        return starlingClient.getAccount();
    }
}

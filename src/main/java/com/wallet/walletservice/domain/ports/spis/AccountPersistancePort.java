package com.wallet.walletservice.domain.ports.spis;

import com.wallet.walletservice.domain.model.Account;

import java.util.Optional;

public interface AccountPersistancePort {
    Optional<Account> saveAccount(Account account);
}

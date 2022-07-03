package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.ports.spis.AccountPersistancePort;
import com.wallet.walletservice.infrastructure.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountPersistanceAdapter implements AccountPersistancePort {

    private AccountRepository accountRepository;

    @Autowired
    public AccountPersistanceAdapter(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> saveAccount(Account account) {
        return Optional.ofNullable(
                accountRepository.save(
                        new com.wallet.walletservice.infrastructure.entities.Account(account))
                        .toDomain());
    }
}

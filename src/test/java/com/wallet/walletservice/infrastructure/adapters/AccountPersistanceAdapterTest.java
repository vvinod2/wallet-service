package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.infrastructure.entities.Account;
import com.wallet.walletservice.infrastructure.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountPersistanceAdapterTest {

    private AccountRepository accountRepository;

    private AccountPersistanceAdapter accountPersistanceAdapter;

    @BeforeEach
    void initialize() {
        accountRepository = Mockito.mock(AccountRepository.class);
        accountPersistanceAdapter = new AccountPersistanceAdapter(accountRepository);
    }

    @Test
    void testSaveAccount() {
        Mockito.when(accountRepository.save(Mockito.any())).thenReturn(new Account());
        assertThat(accountPersistanceAdapter
                .saveAccount(new com.wallet.walletservice.domain.model.Account())).isNotNull();
    }
}

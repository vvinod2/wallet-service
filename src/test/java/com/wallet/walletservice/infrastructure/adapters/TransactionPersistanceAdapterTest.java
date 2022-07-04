package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.infrastructure.entities.Transaction;
import com.wallet.walletservice.infrastructure.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionPersistanceAdapterTest {

    private TransactionRepository transactionRepository;
    private TransactionPersistanceAdapter transactionPersistanceAdapter;

    @BeforeEach
    void initialize() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionPersistanceAdapter = new TransactionPersistanceAdapter(transactionRepository);
    }

    @Test
    void testFindTransactionByTransactionKey() {
        Mockito.when(transactionRepository.
                findByTransactionKey(Mockito.any())).thenReturn(Optional.of(new Transaction()));
        assertThat(transactionPersistanceAdapter
                .findTransactionByTransactionKey("tk1"))
                .isNotEmpty();
    }

    @Test
    void testFindTransactionByTransactionKeyWhenTransactionDoesNotExist() {
        Mockito.when(transactionRepository.
                findByTransactionKey(Mockito.any())).thenReturn(Optional.empty());
        assertThat(transactionPersistanceAdapter
                .findTransactionByTransactionKey("tk1"))
                .isEmpty();
    }

    @Test
    void testGetAllTransactionOfAnAccount() {
        Mockito.when(transactionRepository
                .findAllByAccountOrderByTransactionDateTimeDesc(Mockito.any())).thenReturn(Arrays.asList(new Transaction()));
        assertThat(transactionPersistanceAdapter
                .getAllTransactionOfAnAccount(new Account()))
                .isNotEmpty();
    }
}

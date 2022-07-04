package com.wallet.walletservice.domain.adapters;

import com.wallet.walletservice.domain.WalletServiceException;
import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.Player;
import com.wallet.walletservice.domain.model.Transaction;
import com.wallet.walletservice.domain.ports.apis.WalletServicePort;
import com.wallet.walletservice.domain.ports.spis.AccountPersistancePort;
import com.wallet.walletservice.domain.ports.spis.PlayerPersistancePort;
import com.wallet.walletservice.domain.ports.spis.TransactionPersistancePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletServiceAdapterTest {

    private WalletServicePort walletServicePort;
    private TransactionPersistancePort transactionPort;
    private AccountPersistancePort accountPort;
    private PlayerPersistancePort playerPort;

    @BeforeEach
    void initialize() {
        transactionPort = Mockito.mock(TransactionPersistancePort.class);
        accountPort = Mockito.mock(AccountPersistancePort.class);
        playerPort = Mockito.mock(PlayerPersistancePort.class);
        walletServicePort = new WalletServiceAdapter(transactionPort, playerPort, accountPort);
    }

    @Test()
    void testAddTransactionFailureWhenTransactionKeyAlreadyExists() {
        Mockito.when(
                transactionPort.findTransactionByTransactionKey(
                        Mockito.any()))
                .thenReturn(Optional.of(new Transaction()));

        WalletServiceException exception = assertThrows(
                WalletServiceException.class,
                () -> walletServicePort.addTransaction(
                        1l,
                        Transaction.builder().transactionKey("123").build()));
        assertThat(exception.getMessage()).isEqualTo("Transaction key already available");
    }

    @Test()
    void testAddTransactionFailureWhenProvidedInvalidPlayerId() {
        Mockito.when(
                transactionPort.findTransactionByTransactionKey(
                                Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(
                playerPort.findPlayerByPlayerId(
                        Mockito.any()))
                .thenReturn(Optional.empty());

        WalletServiceException exception = assertThrows(
                WalletServiceException.class,
                () -> walletServicePort.addTransaction(
                        1l,
                        Transaction.builder().transactionKey("123").build()));
        assertThat(exception.getMessage()).isEqualTo("Specified player does not exists");
    }

    @Test()
    void testAddTransactionFailureWhenAccountNotAddedToPlayer() {
        Mockito.when(
                        transactionPort.findTransactionByTransactionKey(
                                Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(
                        playerPort.findPlayerByPlayerId(
                                Mockito.any()))
                .thenReturn(Optional.of(Player.builder().playerId(1l).build()));

        WalletServiceException exception = assertThrows(
                WalletServiceException.class,
                () -> walletServicePort.addTransaction(
                        1l,
                        Transaction.builder().transactionKey("123").build()));
        assertThat(exception.getMessage()).isEqualTo("Account not associated to the player. Hence action cannot be completed");
    }

    @Test()
    void testAddTransactionFailureWhenDebitAmountGreaterThanBalance() {
        Mockito.when(
                        transactionPort.findTransactionByTransactionKey(
                                Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(
                        playerPort.findPlayerByPlayerId(
                                Mockito.any()))
                .thenReturn(Optional.of(Player.builder().playerId(1l)
                        .account(Account.builder().balance(50d).build()).build()));

        Mockito.when(
                transactionPort.getAllTransactionOfAnAccount(
                        Mockito.any()))
                .thenReturn(Arrays.asList(Transaction.builder().amount(20d)
                        .build()));

        WalletServiceException exception = assertThrows(
                WalletServiceException.class,
                () -> walletServicePort.addTransaction(
                        1l,
                        Transaction.builder().transactionKey("123").amount(-100d).build()));
        assertThat(exception.getMessage()).isEqualTo("Balance insufficient to transact");
    }

    @Test()
    void testAddTransactionSuccess() {
        Mockito.when(
                        transactionPort.findTransactionByTransactionKey(
                                Mockito.any()))
                .thenReturn(Optional.empty());

        Mockito.when(
                        playerPort.findPlayerByPlayerId(
                                Mockito.any()))
                .thenReturn(Optional.of(Player.builder().playerId(1l)
                        .account(Account.builder().balance(50d).build()).build()));

        Mockito.when(
                        transactionPort.getAllTransactionOfAnAccount(
                                Mockito.any()))
                .thenReturn(Arrays.asList(Transaction.builder().amount(20d)
                        .build()));

        walletServicePort.addTransaction(
                1l,
                Transaction.builder().transactionKey("123").amount(100d).build());
        Mockito.verify(accountPort, Mockito.times(1)).saveAccount(Mockito.any());
        Mockito.verify(transactionPort, Mockito.times(1)).saveTransaction(Mockito.any());

    }

    @Test()
    void testGetAllTransactionHistorySuccess() {

        Mockito.when(
                        playerPort.findPlayerByPlayerId(
                                Mockito.any()))
                .thenReturn(Optional.of(Player.builder().playerId(1l)
                        .account(Account.builder().balance(50d).build()).build()));

        Mockito.when(
                        transactionPort.getAllTransactionOfAnAccount(
                                Mockito.any()))
                .thenReturn(Arrays.asList(Transaction.builder().amount(20d)
                        .build()));

        assertThat(walletServicePort.getAllTransactionHistory(1l).size()).isEqualTo(1);

    }

    @Test()
    void testGetAccountDetailsOfAPlayerSuccess() {

        Mockito.when(
                        playerPort.findPlayerByPlayerId(
                                Mockito.any()))
                .thenReturn(Optional.of(Player.builder().playerId(1l)
                        .account(Account.builder().balance(0d).build()).build()));

        Mockito.when(
                        transactionPort.getAllTransactionOfAnAccount(
                                Mockito.any()))
                .thenReturn(Arrays.asList(Transaction.builder().amount(20d)
                        .build()));

        assertThat(walletServicePort.getAccountDetailsOfAPlayer(1l).getBalance()).isEqualTo(20d);

    }
}

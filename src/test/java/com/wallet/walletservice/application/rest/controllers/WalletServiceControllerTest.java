package com.wallet.walletservice.application.rest.controllers;

import com.wallet.walletservice.application.rest.model.Transaction;
import com.wallet.walletservice.domain.WalletServiceException;
import com.wallet.walletservice.domain.adapters.WalletServiceAdapter;
import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.CommonResponse;
import com.wallet.walletservice.domain.ports.apis.WalletServicePort;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletServiceControllerTest {

    private WalletServiceController walletServiceController;
    @Mock
    private WalletServicePort walletServicePort;

    @BeforeEach
    void initialize() {
        walletServicePort = Mockito.mock(WalletServiceAdapter.class);
        walletServiceController = new WalletServiceController(walletServicePort);
    }

    @Test
    @Order(1)
    void testAddTransaction() {
        Mockito.when(
                walletServicePort.addTransaction(
                        Mockito.any(),Mockito.any()))
                .thenReturn(
                        new CommonResponse("Successfully completed the transaction"));
        assertThat(
                walletServiceController.addTransaction(1l, new Transaction())
                        .getBody().getMessage())
                .isEqualTo("Successfully completed the transaction");
    }

    @Test
    @Order(2)
    void testAddTransactionWhenTheAdapterThrowsWalletServiceException() {
        Mockito.when(
                        walletServicePort.addTransaction(
                                Mockito.any(),Mockito.any()))
                .thenThrow(new WalletServiceException("Transaction Id already available"));
        assertThat(
                walletServiceController.addTransaction(1l, new Transaction())
                        .getBody().getMessage())
                .isEqualTo("Transaction Id already available");
    }

    @Test
    @Order(3)
    void testAddTransactionWhenTheAdapterThrowsUnknownException() {
        Mockito.when(
                        walletServicePort.addTransaction(
                                Mockito.any(),Mockito.any()))
                .thenThrow(new NullPointerException("Null Pointer Exception"));
        assertThat(
                walletServiceController.addTransaction(1l, new Transaction())
                        .getBody().getMessage())
                .isEqualTo("Transaction is not successful");
    }

    @Test
    @Order(4)
    void testGetAllTransactionOfPlayer() {
        walletServicePort = Mockito.mock(WalletServiceAdapter.class);
        walletServiceController = new WalletServiceController(walletServicePort);
        Mockito.when(
                        walletServicePort.getAllTransactionHistory(
                                Mockito.any()))
                .thenReturn(
                        Arrays.asList(com.wallet.walletservice.domain.model.Transaction.builder()
                                .account(Account.builder().accountId(1l)
                                        .build())
                                .build()));
        assertThat(
                ((CollectionModel<List<Transaction>>)walletServiceController.getAllTransactionsOfAPlayer(1l)
                        .getBody()).getContent().size())
                .isEqualTo(1);
    }

    @Test
    @Order(5)
    void testGetAllTransactionOfPlayerWhenTheAdapterThrowsWalletServiceException() {
        Mockito.when(
                        walletServicePort.getAllTransactionHistory(
                                Mockito.any()))
                .thenThrow(new WalletServiceException("Specified player does not exists"));
        assertThat(
                ((com.wallet.walletservice.application.rest.model.CommonResponse)(walletServiceController
                        .getAllTransactionsOfAPlayer(1l))
                        .getBody()).getMessage())
                .isEqualTo("Specified player does not exists");
    }

    @Test
    @Order(6)
    void testGetAllTransactionOfPlayerWhenTheAdapterThrowsUnknownException() {
        Mockito.when(
                        walletServicePort.getAllTransactionHistory(
                                Mockito.any()))
                .thenThrow(new NullPointerException("Null Pointer Exception"));
        assertThat(
                ((com.wallet.walletservice.application.rest.model.CommonResponse)(walletServiceController
                        .getAllTransactionsOfAPlayer(1l))
                        .getBody()).getMessage())
                .isEqualTo("Failed to fetch");
    }

    @Test
    @Order(7)
    void testGetAccountDetails() {
        walletServicePort = Mockito.mock(WalletServiceAdapter.class);
        walletServiceController = new WalletServiceController(walletServicePort);
        Mockito.when(
                        walletServicePort.getAccountDetailsOfAPlayer(
                                Mockito.any()))
                .thenReturn(
                        com.wallet.walletservice.domain.model.Account.builder().accountId(1l)
                                        .build());
        assertThat(
                ((EntityModel<com.wallet.walletservice.application.rest.model.Account>)walletServiceController
                        .getAccountDetails(1l)
                        .getBody()).getContent().getAccountId())
                .isEqualTo(1l);
    }

    @Test
    @Order(8)
    void testGetAccountDetailsWhenTheAdapterThrowsWalletServiceException() {
        Mockito.when(
                        walletServicePort.getAccountDetailsOfAPlayer(
                                Mockito.any()))
                .thenThrow(new WalletServiceException("Specified player does not exists"));
        assertThat(
                ((com.wallet.walletservice.application.rest.model.CommonResponse)(walletServiceController
                        .getAccountDetails(1l))
                        .getBody()).getMessage())
                .isEqualTo("Specified player does not exists");
    }

    @Test
    @Order(9)
    void testGetAccountDetailsWhenTheAdapterThrowsUnknownException() {
        Mockito.when(
                        walletServicePort.getAllTransactionHistory(
                                Mockito.any()))
                .thenThrow(new NullPointerException("Null Pointer Exception"));
        assertThat(
                ((com.wallet.walletservice.application.rest.model.CommonResponse)(walletServiceController
                        .getAccountDetails(1l))
                        .getBody()).getMessage())
                .isEqualTo("Failed to fetch");
    }
}

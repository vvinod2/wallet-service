package com.wallet.walletservice.domain.adapters;

import com.wallet.walletservice.domain.WalletServiceException;
import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.CommonResponse;
import com.wallet.walletservice.domain.model.Player;
import com.wallet.walletservice.domain.model.Transaction;
import com.wallet.walletservice.domain.ports.apis.TransactionPort;
import com.wallet.walletservice.domain.ports.spis.AccountPersistancePort;
import com.wallet.walletservice.domain.ports.spis.PlayerPersistancePort;
import com.wallet.walletservice.domain.ports.spis.TransactionPersistancePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransactionAdapter implements TransactionPort {

    private TransactionPersistancePort transactionPersistancePort;

    private PlayerPersistancePort playerPersistancePort;

    private AccountPersistancePort accountPersistancePort;


    @Autowired
    public TransactionAdapter(TransactionPersistancePort transactionPersistancePort,
                              PlayerPersistancePort playerPersistancePort,
                              AccountPersistancePort accountPersistancePort) {
        this.transactionPersistancePort = transactionPersistancePort;
        this.playerPersistancePort = playerPersistancePort;
        this.accountPersistancePort = accountPersistancePort;
    }

    @Override
    @Transactional
    public CommonResponse addTransaction(Long playerId, Transaction transaction) {
        if (transactionPersistancePort
                .findTransactionByTransactionKey(
                        transaction.getTransactionKey())
                .isPresent()) {
            throw new WalletServiceException("Transaction key already available");
        }
        Account account = validateAssociationsAndReturnAccount(playerId);
        Double currentBalance = calculateBalanceFromTransactions(account);
        if (!checkBalanceEligibility(currentBalance, transaction.getAmount())) {
            throw new WalletServiceException("Balance insufficient to transact");
        }
        transaction.setAccount(account);
        transaction.setSavedCurrencyType(transaction.getInputCurrencyType());
        if (transaction.getTransactionDateTime() == null) {
            transaction.setTransactionDateTime(
                    ZonedDateTime.of(
                            LocalDate.now(),
                            LocalTime.now(),
                            ZoneId.systemDefault()));
        }
        account.setBalance(currentBalance + transaction.getAmount());
        account.setCurrency(transaction.getSavedCurrencyType());
        transactionPersistancePort.saveTransaction(transaction);
        accountPersistancePort.saveAccount(account);
        return CommonResponse.builder()
                .message("Successfully completed the transaction")
                .build();
    }

    @Override
    public List<Transaction> getAllTransactionHistory(Long playerId) {
        return getAllTransactionsInAnAccount(validateAssociationsAndReturnAccount(playerId));
    }

    @Override
    public Account getAccountDetailsOfAPlayer(Long playerId) {
        Account account = validateAssociationsAndReturnAccount(playerId);
        account.setBalance(calculateBalanceFromTransactions(account));
        return account;
    }

    private Double calculateBalanceFromTransactions(Account account) {
        return transactionPersistancePort.getAllTransactionOfAnAccount(account)
                .stream()
                .filter(transaction -> transaction.getAmount() != null)
                .collect(Collectors.summingDouble(Transaction::getAmount));
    }

    private boolean checkBalanceEligibility(Double currentBalance, Double transactionAmount) {
        if (currentBalance + transactionAmount > 0) {
            return true;
        }
        return false;
    }

    private Account validateAssociationsAndReturnAccount(Long playerId) {
        Optional<Player> playerOptional = playerPersistancePort.findPlayerByPlayerId(playerId);
        if (playerOptional.isEmpty()) {
            throw new WalletServiceException("Specified player does not exists");
        }
        Player player = playerOptional.get();
        if (player.getAccount() == null) {
            throw new WalletServiceException("Account not associated to the player. Hence action cannot be completed");
        }
        return player.getAccount();
    }

    private List<Transaction> getAllTransactionsInAnAccount(Account account) {
        return transactionPersistancePort
                .getAllTransactionOfAnAccount(account)
                .stream()
                .collect(Collectors.toList());
    }
}

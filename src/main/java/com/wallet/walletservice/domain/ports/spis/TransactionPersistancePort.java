package com.wallet.walletservice.domain.ports.spis;


import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionPersistancePort {

    Optional<Transaction> findTransactionByTransactionKey(String transactionKey);
    Optional<Transaction> saveTransaction(Transaction transaction);

    List<Transaction> getAllTransactionOfAnAccount(Account account);
}

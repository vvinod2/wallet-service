package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.Transaction;
import com.wallet.walletservice.domain.ports.spis.TransactionPersistancePort;
import com.wallet.walletservice.infrastructure.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransactionPersistanceAdapter implements TransactionPersistancePort {

    private TransactionRepository transactionRepository;


    @Autowired
    public TransactionPersistanceAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<Transaction> findTransactionByTransactionKey(String transactionKey) {
        Optional<com.wallet.walletservice.infrastructure.entities.Transaction> transactionEntity
                = transactionRepository.findByTransactionKey(transactionKey);
        if (transactionEntity.isPresent()){
            return Optional.of(transactionEntity.get().toDomain());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> saveTransaction(Transaction transaction) {
        return Optional.ofNullable(
                transactionRepository.save(
                        new com.wallet.walletservice.infrastructure.entities.Transaction(transaction))
                        .toDomain());
    }

    @Override
    public List<Transaction> getAllTransactionOfAnAccount(Account account) {
        return transactionRepository.findAllByAccountOrderByTransactionDateTimeDesc(
                new com.wallet.walletservice.infrastructure.entities.Account(account))
                .stream()
                .map(transaction -> transaction.toDomain())
                .collect(Collectors.toList());
    }
}

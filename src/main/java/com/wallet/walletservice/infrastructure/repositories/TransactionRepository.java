package com.wallet.walletservice.infrastructure.repositories;

import com.wallet.walletservice.infrastructure.entities.Account;
import com.wallet.walletservice.infrastructure.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionKey(String transactionKey);
    List<Transaction> findAllByAccountOrderByTransactionDateTimeDesc(Account account);
}

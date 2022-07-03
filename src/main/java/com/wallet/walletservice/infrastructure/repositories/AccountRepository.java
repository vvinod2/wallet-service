package com.wallet.walletservice.infrastructure.repositories;

import com.wallet.walletservice.infrastructure.entities.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}

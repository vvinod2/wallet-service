package com.wallet.walletservice.domain.ports.apis;

import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.CommonResponse;
import com.wallet.walletservice.domain.model.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TransactionPort {

    CommonResponse addTransaction(Long playerId, Transaction transaction);

    List<Transaction> getAllTransactionHistory(Long playerId);

    Account getAccountDetailsOfAPlayer(Long playerId);
}

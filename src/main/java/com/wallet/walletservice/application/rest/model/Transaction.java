package com.wallet.walletservice.application.rest.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Transaction {

    private Long transactionId;

    @NotNull
    private String transactionKey;
    @NotNull
    private String inputCurrencyType;

    private String savedCurrencyType;

    @NotNull
    private Double amount;

    private Account account;

    private ZonedDateTime transactionDateTime;

    private String transactionNote;

    public Transaction(com.wallet.walletservice.domain.model.Transaction domainTransaction) {
        this.transactionId = domainTransaction.getTransactionId();
        this.transactionKey = domainTransaction.getTransactionKey();
        this.account = new Account(domainTransaction.getAccount());
        this.amount = domainTransaction.getAmount();
        this.inputCurrencyType = domainTransaction.getInputCurrencyType();
        this.savedCurrencyType = domainTransaction.getSavedCurrencyType();
        this.transactionDateTime = domainTransaction.getTransactionDateTime();
        this.transactionNote = domainTransaction.getTransactionNote();
    }

    public com.wallet.walletservice.domain.model.Transaction toDomain() {
        return com.wallet.walletservice.domain.model.Transaction.builder()
                .transactionId(this.transactionId)
                .transactionKey(this.transactionKey)
                .account(getDomainAccount())
                .amount(this.amount)
                .inputCurrencyType(this.inputCurrencyType)
                .savedCurrencyType(this.savedCurrencyType)
                .transactionDateTime(this.transactionDateTime)
                .transactionNote(this.transactionNote)
                .build();
    }

    private com.wallet.walletservice.domain.model.Account getDomainAccount() {
        if (this.account != null) {
            return this.account.toDomain();
        }
        return null;
    }
}

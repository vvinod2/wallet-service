package com.wallet.walletservice.infrastructure.entities;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(unique = true, name = "transaction_key")
    private String transactionKey;

    @Column(name = "input_currency_type")
    private String inputCurrencyType;

    @Column(name="saved_currency_type")
    private String savedCurrencyType;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name="transaction_date_time")
    private ZonedDateTime transactionDateTime;

    @Column(name="transaction_note")
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

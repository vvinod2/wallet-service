package com.wallet.walletservice.infrastructure.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_description")
    private String accountDescription;

    @Column(name = "balance", columnDefinition = "DOUBLE PRECISION CHECK (balance >= 0)")
    private Double balance;

    @Column(name = "currency")
    private String currency;

    public Account(com.wallet.walletservice.domain.model.Account domainAccount) {
        this.accountId = domainAccount.getAccountId();
        this.accountDescription = domainAccount.getAccountDescription();
        this.balance = domainAccount.getBalance();
        this.currency = domainAccount.getCurrency();
    }

    public com.wallet.walletservice.domain.model.Account toDomain() {
        return com.wallet.walletservice.domain.model.Account.builder()
                .accountId(this.accountId)
                .accountDescription(this.accountDescription)
                .balance(this.balance)
                .currency(this.currency).build();
    }

}

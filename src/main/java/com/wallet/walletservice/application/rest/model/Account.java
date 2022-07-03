package com.wallet.walletservice.application.rest.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Account {
    private Long accountId;
    private String accountDescription;

    private Double balance;

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
                .currency(this.currency)
                .build();
    }

}

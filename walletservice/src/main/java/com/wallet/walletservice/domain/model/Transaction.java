package com.wallet.walletservice.domain.model;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Transaction{

    private Long transactionId;

    private String transactionKey;

    private String inputCurrencyType;

    private String savedCurrencyType;

    private Double amount;

    private Account account;

    private ZonedDateTime transactionDateTime;
}

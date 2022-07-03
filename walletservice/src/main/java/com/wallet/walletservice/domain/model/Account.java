package com.wallet.walletservice.domain.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

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
}

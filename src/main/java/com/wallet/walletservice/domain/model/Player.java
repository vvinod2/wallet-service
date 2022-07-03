package com.wallet.walletservice.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Player {
    private Long playerId;

    private String playerName;

    private Account account;

}

package com.wallet.walletservice.application.rest.model;

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

    public Player(com.wallet.walletservice.domain.model.Player domainPlayer) {
        this.playerId = domainPlayer.getPlayerId();
        this.playerName = domainPlayer.getPlayerName();
        this.account = new Account(domainPlayer.getAccount());
    }

    public com.wallet.walletservice.domain.model.Player toDomain() {
        return com.wallet.walletservice.domain.model.Player.builder()
                .playerId(this.playerId)
                .playerName(this.playerName)
                .account(getDomainAccount())
                .build();
    }

    private com.wallet.walletservice.domain.model.Account getDomainAccount() {
        if (this.account != null) {
            return this.account.toDomain();
        }
        return null;
    }

}

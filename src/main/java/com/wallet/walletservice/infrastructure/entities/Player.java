package com.wallet.walletservice.infrastructure.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "player_name")
    private String playerName;

    @OneToOne
    @JoinColumn(name = "account_id")
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

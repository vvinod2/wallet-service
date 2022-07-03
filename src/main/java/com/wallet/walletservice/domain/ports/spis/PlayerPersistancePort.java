package com.wallet.walletservice.domain.ports.spis;

import com.wallet.walletservice.domain.model.Account;
import com.wallet.walletservice.domain.model.Player;

import java.util.Optional;

public interface PlayerPersistancePort {
    Optional<Player> findPlayerByPlayerId(Long playerId);
}

package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.domain.model.Player;
import com.wallet.walletservice.domain.ports.spis.PlayerPersistancePort;
import com.wallet.walletservice.infrastructure.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerPersistanceAdapter implements PlayerPersistancePort {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerPersistanceAdapter(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Optional<Player> findPlayerByPlayerId(Long playerId) {
        Optional<com.wallet.walletservice.infrastructure.entities.Player> playerEntityOptional =
                playerRepository.findById(playerId);
        if (playerEntityOptional.isPresent()) {
            return  Optional.of(playerEntityOptional.get().toDomain());
        }
        return Optional.empty();
    }

}

package com.wallet.walletservice.infrastructure.adapters;

import com.wallet.walletservice.infrastructure.entities.Player;
import com.wallet.walletservice.infrastructure.repositories.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerPersistanceAdapterTest {

    private PlayerPersistanceAdapter playerPersistanceAdapter;
    private PlayerRepository playerRepository;

    @BeforeEach
    void initialize() {
        playerRepository = Mockito.mock(PlayerRepository.class);
        playerPersistanceAdapter = new PlayerPersistanceAdapter(playerRepository);
    }

    @Test
    void testFindPlayerByPlayerId() {
        Mockito.when(playerRepository.findById(Mockito.any())).thenReturn(Optional.of(new Player()));
        assertThat(playerPersistanceAdapter.findPlayerByPlayerId(1l)).isNotEmpty();
    }

    @Test
    void testFindPlayerByPlayerIdWhenPlayerDoesNotExist() {
        Mockito.when(playerRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThat(playerPersistanceAdapter.findPlayerByPlayerId(1l)).isEmpty();
    }
}

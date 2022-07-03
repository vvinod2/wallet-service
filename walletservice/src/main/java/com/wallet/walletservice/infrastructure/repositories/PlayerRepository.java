package com.wallet.walletservice.infrastructure.repositories;


import com.wallet.walletservice.infrastructure.entities.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}

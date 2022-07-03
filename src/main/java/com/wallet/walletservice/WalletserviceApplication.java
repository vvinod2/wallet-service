package com.wallet.walletservice;

import com.wallet.walletservice.infrastructure.entities.Account;
import com.wallet.walletservice.infrastructure.entities.Player;
import com.wallet.walletservice.infrastructure.entities.Transaction;
import com.wallet.walletservice.infrastructure.repositories.AccountRepository;
import com.wallet.walletservice.infrastructure.repositories.PlayerRepository;
import com.wallet.walletservice.infrastructure.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
public class WalletserviceApplication implements CommandLineRunner {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	public static void main(String[] args) {
		SpringApplication.run(WalletserviceApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		//Anything if needed to be loaded in future;
	}


}

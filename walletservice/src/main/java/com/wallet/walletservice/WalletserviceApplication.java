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
		//createSampleData();
	}

	private void createSampleData() {
		Account account1 = new Account(null, "test acc 1", null, null);
		accountRepository.save(account1);
		Player player1 = new Player(null, "pl1 name", account1);
		playerRepository.save(player1);
		Transaction transaction1 = new Transaction(null, "ts1", "GBP", "GBP", 100.50d, account1, ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.systemDefault()));
		Transaction transaction2 = new Transaction(null, "ts2", "GBP", "GBP", 100.50d, account1, ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.systemDefault()));
		transactionRepository.save(transaction1);
		transactionRepository.save(transaction2);
	}


}

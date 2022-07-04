package com.wallet.walletservice;

import com.wallet.walletservice.application.rest.controllers.WalletServiceController;
import com.wallet.walletservice.application.rest.model.Transaction;
import com.wallet.walletservice.infrastructure.entities.Account;
import com.wallet.walletservice.infrastructure.entities.Player;
import com.wallet.walletservice.infrastructure.repositories.AccountRepository;
import com.wallet.walletservice.infrastructure.repositories.PlayerRepository;
import com.wallet.walletservice.infrastructure.repositories.TransactionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WalletserviceApplicationTests {

	@Autowired
	private WalletServiceController controller;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PlayerRepository playerRepository;

	private com.wallet.walletservice.domain.model.Player mike;

	private com.wallet.walletservice.domain.model.Player lilly;
	private com.wallet.walletservice.domain.model.Account mikesAccount;


	@BeforeAll
	public void initialize() {

		mikesAccount = accountRepository.save(
				new Account(
						null,
						"Mike's account",
						0d,
						"GBP"
				)).toDomain();

		mike = playerRepository.save(
				new Player(
						null,
						"Mike",
						new Account(mikesAccount)
				)).toDomain();

		lilly = playerRepository.save(
				new Player(
						null,
						"Lilly",
						null
				)).toDomain();
	}

	@Test
	@Order(1)
	void testCredit() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk1",
						"GBP",
						null,
						100d,
						null,
						null,
						" A credit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(100d);
	}

	@Test
	@Order(2)
	void testDebit() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk2",
						"GBP",
						null,
						-50d,
						null,
						null,
						" A debit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(50d);
	}

	@Test
	@Order(3)
	void testDebitBelowTheBalance() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk3",
						"GBP",
						null,
						-100d,
						null,
						null,
						" A debit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(50d);
	}

	@Test
	@Order(4)
	void testTransactionWhichMakesTheBalanceZero() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk4",
						"GBP",
						null,
						-50d,
						null,
						null,
						" A debit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(0);
	}

	@Test
	@Order(5)
	void testTransactionFailureOnUsingSameTransactionKey() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk4",
						"GBP",
						null,
						50d,
						null,
						null,
						" A credit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(0);
	}

	@Test
	@Order(6)
	void testTransactionFailureOnNotProvidingTransactionAmount() {
		controller.addTransaction(mike.getPlayerId(),
				new Transaction(null,
						"tk5",
						"GBP",
						null,
						null,
						null,
						null,
						" A credit"));
		assertThat(
				((EntityModel<com.wallet.walletservice.application.rest.model.Account>)controller
						.getAccountDetails(mike.getPlayerId()).getBody())
						.getContent()
						.getBalance())
				.isEqualTo(0);
	}

	@Test
	@Order(7)
	void testTransactionFailureOnTransactingForPlayerWithoutAccount() {
		int intialNoOfTransaction = StreamSupport.stream(
				transactionRepository.findAll().spliterator(), false)
				.collect(Collectors.toList()).size();
		controller.addTransaction(lilly.getPlayerId(),
				new Transaction(null,
						"tk6",
						"GBP",
						null,
						100d,
						null,
						null,
						" A credit"));
		assertThat(
				StreamSupport.stream(
								transactionRepository.findAll().spliterator(), false)
						.collect(Collectors.toList()).size())
				.isEqualTo(intialNoOfTransaction);
	}

	@Test
	@Order(8)
	void testTransactionFailureOnTransactingForUnknownPlayer() {
		int intialNoOfTransaction = StreamSupport.stream(
						transactionRepository.findAll().spliterator(), false)
				.collect(Collectors.toList()).size();
		controller.addTransaction(999999l,
				new Transaction(null,
						"tk6",
						"GBP",
						null,
						100d,
						null,
						null,
						" A credit"));
		assertThat(
				StreamSupport.stream(
								transactionRepository.findAll().spliterator(), false)
						.collect(Collectors.toList()).size())
				.isEqualTo(intialNoOfTransaction);
	}

	@Test
	@Order(9)
	void testGettingTransactionHistory() {
		assertThat(((CollectionModel<Transaction>)controller
				.getAllTransactionsOfAPlayer(
						mike.getPlayerId()).getBody()).getContent().isEmpty())
				.isFalse();
	}
}

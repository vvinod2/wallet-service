package com.wallet.walletservice.application.rest.controllers;

import com.wallet.walletservice.application.rest.model.Account;
import com.wallet.walletservice.application.rest.model.CommonResponse;
import com.wallet.walletservice.application.rest.model.Transaction;
import com.wallet.walletservice.domain.WalletServiceException;
import com.wallet.walletservice.domain.ports.apis.WalletServicePort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@Api(produces = "application/json", value = "Operations to manage credit and debit transactions of a player")
public class WalletServiceController {

    private WalletServicePort walletServicePort;


    @Autowired
    public WalletServiceController(WalletServicePort walletServicePort) {
        this.walletServicePort = walletServicePort;
    }

    @PostMapping(path = "/player/{playerId}/transaction")
    @ApiOperation(value = "To do a credit or debit", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully completed the transaction"),
            @ApiResponse(code = 500, message = "Transaction key already available |" +
                    " Balance insufficient to transact | Specified player does not exists | Account not associated to " +
                    "the player. Hence action cannot be completed | Transaction is not successful")
    }
    )
    public ResponseEntity<CommonResponse> addTransaction(@PathVariable(name = "playerId") Long playerId,
                                                         @RequestBody @Valid Transaction transaction) {
        try {
            return new ResponseEntity<>(
                    new CommonResponse(walletServicePort.addTransaction(playerId,
                            transaction.toDomain())),
                    HttpStatus.CREATED);
        } catch (WalletServiceException walletServiceException) {
            return new ResponseEntity<>(CommonResponse.builder().message(walletServiceException.getMessage()).build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message("Transaction is not successful")
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/player/{playerId}/transactions")
    @ApiOperation(value = "To get the transaction history of a player", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200 , message = "Successfully fetched all the transactions"),
            @ApiResponse(code = 500, message = "Failed to fetch")
    }
    )
    public ResponseEntity getAllTransactionsOfAPlayer(@PathVariable(name = "playerId") Long playerId) {
        try {
            return new ResponseEntity(
                    CollectionModel.of(walletServicePort.getAllTransactionHistory(playerId)
                    .stream()
                    .map(Transaction::new)
                    .map(transaction -> EntityModel.of(transaction))
                    .map(transactionEntityModel -> transactionEntityModel
                            .add(Link.of(
                                            getHrefLinkForTransaction(transactionEntityModel
                                                    .getContent()
                                                    .getTransactionId()))
                                    .withSelfRel()))
                    .map(transactionEntityModel -> transactionEntityModel
                            .add(Link.of(
                                            getHrefLinkForAccount(
                                                    transactionEntityModel
                                                            .getContent()
                                                            .getAccount()
                                                            .getAccountId()))
                                    .withRel("account")))
                    .collect(Collectors.toList())), HttpStatus.OK);
        } catch (WalletServiceException walletServiceException) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message(walletServiceException.getMessage())
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message("Failed to fetch")
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/player/{playerId}/account/details")
    @ApiOperation(value = "To get the balance and other account details of a player", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched all the transactions"),
            @ApiResponse(code = 500, message = "Specified player does not exists | Account not associated to the " +
                    "player. Hence action cannot be completed | Failed to fetch"),
    }
    )
    public ResponseEntity getAccountDetails(@PathVariable(name = "playerId") Long playerId) {
        try {
            Account account = new Account(walletServicePort.getAccountDetailsOfAPlayer(playerId));
            return new ResponseEntity<>( EntityModel.of(account)
                    .add(Link.of(
                            getHrefLinkForAccount(account.getAccountId()))
                            .withSelfRel()),
                    HttpStatus.OK);
        } catch (WalletServiceException walletServiceException) {
            return new ResponseEntity<>(
                    CommonResponse.builder()
                            .message(walletServiceException.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exception) {
            return new ResponseEntity<>(CommonResponse.builder()
                    .message("Failed to fetch")
                    .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getHrefLinkForTransaction(Long transactionId) {
        return  new StringBuffer()
                .append("http://localhost:8080/walletservice/transactions/")
                .append(transactionId)
                .toString();
    }

    private String getHrefLinkForAccount(Long accountId) {
        return  new StringBuffer()
                .append("http://localhost:8080/walletservice/accounts/")
                .append(accountId)
                .toString();
    }
}

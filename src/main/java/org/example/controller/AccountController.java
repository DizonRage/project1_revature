package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.dto.AccountResponse;
import org.example.dto.AddOwnerRequest;
import org.example.dto.DepositRequest;
import org.example.dto.OwnerResponse;
import org.example.dto.TransactionResponse;
import org.example.dto.WithdrawRequest;
import org.example.entity.Account;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.example.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Integer id, HttpSession session) {
        Account account = accountService.getAccount(
                id, SessionUtil.getUserId(session), SessionUtil.getRole(session));
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable Integer id,
                                                                     HttpSession session) {
        List<TransactionResponse> transactions = transactionService
                .getTransactionsForAccount(id, SessionUtil.getUserId(session), SessionUtil.getRole(session))
                .stream()
                .map(TransactionResponse::from)
                .toList();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/{id}/owners")
    public ResponseEntity<OwnerResponse> addOwner(@PathVariable Integer id,
                                                  @Valid @RequestBody AddOwnerRequest request,
                                                  HttpSession session) {
        OwnerResponse response = OwnerResponse.from(accountService.addOwner(
                id, request.username(), SessionUtil.getUserId(session), SessionUtil.getRole(session)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable Integer id,
                                                   @Valid @RequestBody DepositRequest request,
                                                   HttpSession session) {
        Account account = accountService.deposit(
                id, request.amount(), SessionUtil.getUserId(session), SessionUtil.getRole(session));
        return ResponseEntity.ok(AccountResponse.from(account));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable Integer id,
                                                    @Valid @RequestBody WithdrawRequest request,
                                                    HttpSession session) {
        Account account = accountService.withdraw(
                id, request.amount(), SessionUtil.getUserId(session), SessionUtil.getRole(session));
        return ResponseEntity.ok(AccountResponse.from(account));
    }
}

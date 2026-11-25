package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.AccountResponse;
import org.example.dto.DepositRequest;
import org.example.dto.WithdrawRequest;
import org.example.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable Integer id,
                                                   @Valid @RequestBody DepositRequest request) {
        return ResponseEntity.ok(AccountResponse.from(accountService.deposit(id, request.amount())));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable Integer id,
                                                    @Valid @RequestBody WithdrawRequest request) {
        return ResponseEntity.ok(AccountResponse.from(accountService.withdraw(id, request.amount())));
    }
}

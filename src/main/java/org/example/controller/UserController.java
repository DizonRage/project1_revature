package org.example.controller;

import jakarta.servlet.http.HttpSession;
import org.example.dto.AccountResponse;
import org.example.service.AccountService;
import org.example.util.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;

    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/me/accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(HttpSession session) {
        List<AccountResponse> accounts = accountService
                .getAccountsForUser(SessionUtil.getUserId(session))
                .stream()
                .map(AccountResponse::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }
}

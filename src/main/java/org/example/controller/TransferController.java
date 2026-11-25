package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.dto.TransferRequest;
import org.example.dto.TransferResponse;
import org.example.entity.Account;
import org.example.service.AccountService;
import org.example.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final AccountService accountService;

    public TransferController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request,
                                                     HttpSession session) {
        Account from = accountService.transfer(
                request.fromAccountId(), request.toAccountId(), request.amount(),
                SessionUtil.getUserId(session), SessionUtil.getRole(session));
        TransferResponse response = new TransferResponse(
                request.fromAccountId(),
                request.toAccountId(),
                request.amount(),
                from.getBalance(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

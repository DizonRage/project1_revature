package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.dto.AccountResponse;
import org.example.dto.ApplicationResponse;
import org.example.dto.UpdateAccountStatusRequest;
import org.example.dto.UpdateApplicationStatusRequest;
import org.example.dto.UserResponse;
import org.example.service.AccountApplicationService;
import org.example.service.AccountService;
import org.example.service.UserService;
import org.example.util.SessionUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AccountService accountService;
    private final UserService userService;
    private final AccountApplicationService applicationService;

    public AdminController(AccountService accountService,
                           UserService userService,
                           AccountApplicationService applicationService) {
        this.accountService = accountService;
        this.userService = userService;
        this.applicationService = applicationService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAccounts(@RequestParam(required = false) String status,
                                                             HttpSession session) {
        SessionUtil.requireAdmin(session);
        List<AccountResponse> accounts = accountService.getAccounts(status).stream()
                .map(AccountResponse::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateAccountStatusRequest request,
            HttpSession session) {
        SessionUtil.requireAdmin(session);
        return ResponseEntity.ok(
                AccountResponse.from(accountService.updateAccountStatus(id, request.status())));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers(HttpSession session) {
        SessionUtil.requireAdmin(session);
        List<UserResponse> users = userService.listUsers().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplications(
            @RequestParam(required = false) String status,
            HttpSession session) {
        SessionUtil.requireAdmin(session);
        List<ApplicationResponse> applications = applicationService.getApplications(status).stream()
                .map(ApplicationResponse::from)
                .toList();
        return ResponseEntity.ok(applications);
    }

    @PatchMapping("/applications/{id}")
    public ResponseEntity<ApplicationResponse> decideApplication(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateApplicationStatusRequest request,
            HttpSession session) {
        SessionUtil.requireAdmin(session);
        return ResponseEntity.ok(ApplicationResponse.from(applicationService.decideApplication(
                id, request.status(), SessionUtil.getUserId(session))));
    }
}

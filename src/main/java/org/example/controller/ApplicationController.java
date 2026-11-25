package org.example.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.dto.ApplicationRequest;
import org.example.dto.ApplicationResponse;
import org.example.service.AccountApplicationService;
import org.example.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final AccountApplicationService applicationService;

    public ApplicationController(AccountApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> submit(@Valid @RequestBody ApplicationRequest request,
                                                      HttpSession session) {
        ApplicationResponse response = ApplicationResponse.from(applicationService.submitApplication(
                SessionUtil.getUserId(session), request.accountType()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(HttpSession session) {
        List<ApplicationResponse> applications = applicationService
                .getApplicationsForUser(SessionUtil.getUserId(session))
                .stream()
                .map(ApplicationResponse::from)
                .toList();
        return ResponseEntity.ok(applications);
    }
}

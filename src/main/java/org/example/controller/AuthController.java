package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.dto.UserResponse;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(
                request.username(), request.password(),
                request.firstName(), request.lastName(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest servletRequest) {
        User user = userService.authenticate(request.username(), request.password());
        SessionUtil.signIn(servletRequest, user);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
        SessionUtil.signOut(servletRequest);
        return ResponseEntity.noContent().build();
    }
}

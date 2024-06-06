package com.phdang97.bookstore.controller;

import com.phdang97.bookstore.dto.request.LoginRequest;
import com.phdang97.bookstore.dto.request.RegisterRequest;
import com.phdang97.bookstore.dto.response.LoginResponse;
import com.phdang97.bookstore.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  @Autowired private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @GetMapping("/verify")
  public String verifyAccount(@RequestParam(name = "token") @NotBlank String token) {
    return authService.verifyAccount(token);
  }
}

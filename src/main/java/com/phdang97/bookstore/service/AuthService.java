package com.phdang97.bookstore.service;

import com.phdang97.bookstore.dto.request.LoginRequest;
import com.phdang97.bookstore.dto.request.RegisterRequest;
import com.phdang97.bookstore.dto.response.LoginResponse;

public interface AuthService {
  String register(RegisterRequest request);

  LoginResponse login(LoginRequest request);

  String verifyAccount(String token);
}

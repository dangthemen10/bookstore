package com.phdang97.bookstore.service;

import com.phdang97.bookstore.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
  String extractUserName(String token);

  <T> T extractClaim(String token, Function<Claims, T> claimResolver);

  String generateToken(User user);

  String generateToken(Map<String, Object> extraClaims, User user);

  boolean isTokenValid(String token, UserDetails user);
}

package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
  private static final String SECRET_KEY =
      "012ebfa75d0670c8432ade007227194df705bac7529b29016c8f384b2a857c24";

  @Override
  public String extractUserName(String token) {
    return extractClaim(token, (Claims::getSubject));
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  @Override
  public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
    final Claims claims = extractAllClaims(token);
    return claimResolver.apply(claims);
  }

  @Override
  public String generateToken(User user) {
    return generateToken(new HashMap<>(), user);
  }

  @Override
  public String generateToken(Map<String, Object> extraClaims, User user) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
  }

  private boolean isTokenExpired(String token) {
    Date expirationDate = extractExpiration(token);
    return expirationDate.before(new Date());
  }

  @Override
  public boolean isTokenValid(String token, UserDetails user) {
    return user.getUsername().equals(extractUserName(token)) && !isTokenExpired(token);
  }
}

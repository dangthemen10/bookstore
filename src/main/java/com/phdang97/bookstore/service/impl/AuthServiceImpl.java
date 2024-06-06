package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.dto.request.LoginRequest;
import com.phdang97.bookstore.dto.request.RegisterRequest;
import com.phdang97.bookstore.dto.response.LoginResponse;
import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.enums.Role;
import com.phdang97.bookstore.repository.AuthoritiesRepository;
import com.phdang97.bookstore.repository.CartRepository;
import com.phdang97.bookstore.repository.UserRepository;
import com.phdang97.bookstore.service.AuthService;
import com.phdang97.bookstore.service.CartService;
import com.phdang97.bookstore.service.EmailService;
import com.phdang97.bookstore.service.JwtService;
import jakarta.persistence.EntityExistsException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  @Autowired private final UserRepository repository;
  @Autowired private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final EmailService emailService;
  @Autowired private final AuthoritiesRepository authoritiesRepository;
  @Autowired private final CartRepository cartRepository;
  @Autowired private final CartService cartService;

  @Override
  public String register(RegisterRequest request) {
    if (isUserExist(request.getEmail())) {
      throw new EntityExistsException(
          String.format("user with email %s already exist", request.getEmail()));
    }
    String token = UUID.randomUUID().toString();
    var user =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .displayName(request.getFirstName() + " " + request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .isEnabled(true)
            .token(token)
            .authorities(Set.of(authoritiesRepository.findAuthoritiesByRole(Role.USER)))
            .build();
    emailService.sendEmail(request.getFirstName(), request.getEmail(), token);
    user = repository.save(user);
    cartRepository.save(cartService.createCart(null, user));
    return "registered successfully";
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    Authentication cc =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User regUser = repository.findUserByEmail(request.getEmail()).orElse(null);
    String jwtToken = jwtService.generateToken(regUser);
    return new LoginResponse(jwtToken);
  }

  @Override
  public String verifyAccount(String token) {
    User user =
        repository
            .findUserByToken(token)
            .orElseThrow(
                () -> new BadCredentialsException(String.format("token %s not valid", token)));
    if (Boolean.TRUE.equals(user.getIsEnabled())) {
      return "you are already verified your account";
    }
    user.setIsEnabled(true);
    repository.save(user);
    return "Account verified";
  }

  private boolean isUserExist(String email) {
    return repository.findUserByEmail(email).isPresent();
  }
}

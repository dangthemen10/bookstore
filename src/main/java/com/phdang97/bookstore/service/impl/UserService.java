package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.entity.Principal;
import com.phdang97.bookstore.entity.User;
import com.phdang97.bookstore.enums.Role;
import com.phdang97.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {
  @Autowired private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findUserByEmail(email)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("user with email: %s not found", email)));
    return new Principal(user);
  }

  public User getUserById(Integer id) {
    return userRepository
        .findUserById(id)
        .orElseThrow(
            () ->
                new UsernameNotFoundException(String.format("user with email: %s not found", id)));
  }

  public Integer userId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Principal principal = (Principal) authentication.getPrincipal();
    return principal.getId();
  }

  public Integer getCurrent() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Principal principal = (Principal) authentication.getPrincipal();
    boolean isAdmin =
        principal.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_" + Role.ADMIN));
    if (isAdmin) {
      return null;
    }
    return principal.getId();
  }

  public boolean isAnonymousUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication instanceof AnonymousAuthenticationToken;
  }
}

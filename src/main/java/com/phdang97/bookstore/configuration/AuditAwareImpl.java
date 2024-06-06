package com.phdang97.bookstore.configuration;

import com.phdang97.bookstore.entity.Principal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return Optional.empty();
    }
    Principal principal = (Principal) authentication.getPrincipal();
    return Optional.ofNullable(principal.getUsername());
  }
}

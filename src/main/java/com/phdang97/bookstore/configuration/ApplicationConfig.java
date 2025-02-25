package com.phdang97.bookstore.configuration;

import com.phdang97.bookstore.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  @Autowired private final UserService userService;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    var dao = new DaoAuthenticationProvider();
    dao.setUserDetailsService(userService);
    dao.setPasswordEncoder(passwordEncoder());
    return dao;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditAwareImpl();
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}

package com.phdang97.bookstore.configuration;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import com.phdang97.bookstore.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {

  private static final String[] WHITE_LIST = {
    "/api/auth/**",
    "/api/books/image",
    "/api/books/title",
    "/api/books/category",
    "/api/books/details/{id}",
    "/api/categories/all",
    "/api/categories/section"
  };
  private static final String[] USER_LIST = {
    "/api/sections/**",
  };

  private static final String[] ADMIN_LIST = {
    "/api/coupons/**",
    "/api/sections/{name}",
    "/api/categories/{name}",
    "/api/books/{id}",
    "/api/orders/status/{id}"
  };
  private final JwtAuthenticationFilter jwtFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(WHITE_LIST)
                    .permitAll()
                    .requestMatchers(USER_LIST)
                    .hasRole(Role.USER.name())
                    .requestMatchers(ADMIN_LIST)
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers(POST, "/api/sections")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers(POST, "/api/categories")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers(POST, "/api/books")
                    .hasRole(Role.ADMIN.name())
                    .requestMatchers(GET, "/api/wishlists//{wishlist-id}")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

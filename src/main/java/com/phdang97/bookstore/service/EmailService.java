package com.phdang97.bookstore.service;

public interface EmailService {
  void sendEmail(String name, String to, String token);
}

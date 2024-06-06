package com.phdang97.bookstore.service.impl;

import com.phdang97.bookstore.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
  public static final String UTF_8_ENCODING = "UTF-8";
  public static final String EMAIL_TEMPLATE = "emailtemplate";

  @Autowired private final JavaMailSender emailSender;

  @Autowired private final TemplateEngine templateEngine;

  @Value("${spring.mail.verify.host}")
  private String host;

  @Value("${spring.mail.username}")
  private String fromEmail;

  @Override
  @Async
  public void sendEmail(String name, String to, String token) {
    try {
      Context context = new Context();
      context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
      String text = templateEngine.process(EMAIL_TEMPLATE, context);
      MimeMessage message = emailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
      helper.setPriority(1);
      helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
      helper.setFrom(fromEmail);
      helper.setTo(to);
      helper.setText(text, true);
      emailSender.send(message);
    } catch (Exception exception) {
      log.error(exception.getMessage());
    }
  }

  private String getVerificationUrl(String host, String token) {
    return host + "/api/auth/verify?token=" + token;
  }
}

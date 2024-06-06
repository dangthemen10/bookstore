package com.phdang97.bookstore.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public abstract class AbstractBookController {
  protected AbstractBookController() {}
}

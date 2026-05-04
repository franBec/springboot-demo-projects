package dev.pollito.spring_java.sakila.auth.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthWebController {

  @GetMapping("/login")
  public String login() {
    return "auth/login";
  }
}

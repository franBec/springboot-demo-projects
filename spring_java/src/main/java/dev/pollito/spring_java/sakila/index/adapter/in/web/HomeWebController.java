package dev.pollito.spring_java.sakila.index.adapter.in.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeWebController {

  @GetMapping("/")
  public String home() {
    return "home/index";
  }
}

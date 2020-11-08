package spring.security.ex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import spring.security.ex.annotation.SocialUser;
import spring.security.ex.domain.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping
    public String loginComplete(@SocialUser final User user) {
        return "redirect:/board/list";
    }
}

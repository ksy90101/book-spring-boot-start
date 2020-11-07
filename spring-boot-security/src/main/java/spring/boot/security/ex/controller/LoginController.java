package spring.boot.security.ex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import spring.boot.security.ex.annotation.SocialUser;
import spring.boot.security.ex.domain.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/{facebook|google|kakao}/complete") // 인증 성공 후 리다이렉트 경로 설정
    public String loginComplete(@SocialUser final User user) {
        return "redirect:/board/list";
    }
}

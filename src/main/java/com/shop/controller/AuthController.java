package com.shop.controller;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository users, PasswordEncoder encoder){
        this.users=users; this.encoder=encoder;
    }

    @GetMapping("/login")
    public String login(){ return "login"; }

    @GetMapping("/register")
    public String register(){ return "register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username, @RequestParam String password,
                             @RequestParam String fullName, HttpSession session){
        if(users.findByUsername(username).isPresent()) return "redirect:/register?error=exists";
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setFullName(fullName);
        u.setRole("CUSTOMER");
        users.save(u);
        // Đăng ký xong không tự đăng nhập; chuyển khách về trang đăng nhập.
        return "redirect:/login?registered=1";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session){
        return users.findByUsername(username)
                .filter(u -> encoder.matches(password, u.getPassword()))
                .map(u -> { session.setAttribute("user", u); return "redirect:/"; })
                .orElse("redirect:/login?error=1");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){ session.invalidate(); return "redirect:/"; }
}

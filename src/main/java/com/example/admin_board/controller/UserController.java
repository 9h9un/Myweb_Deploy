package com.example.admin_board.controller;

import com.example.admin_board.dto.User;
import com.example.admin_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    //회원가입 폼과 회원가입 처리 기능
    //로그인 폼과 로그인 처리 기능
    // 로그아웃 처리

    @Autowired
    private UserService userService;

    // 회원가입 폼 보여주기 (GET /register)
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // templates/register.html 뷰 반환
    }

    // 회원가입 처리 (POST /register)
    @PostMapping("/register")
    public String register(User user, Model model) {
        // 아이디 중복 확인
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("errorMessage", "이미 사용 중인 아이디입니다.");
            return "register"; // 다시 회원가입 페이지
        }

        // 회원가입 시도
        boolean result = userService.register(user);
        if (result) {
            model.addAttribute("successMessage", "회원가입 성공! 로그인 해주세요.");
        } else {
            model.addAttribute("errorMessage", "회원가입 실패! 다시 시도하세요.");
        }

        return "register"; // 결과 메시지와 함께 회원가입 페이지 유지
    }

    // 로그인 폼 보여주기 (GET /login)
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // templates/login.html 뷰 반환
    }

    // 로그인 처리 (POST /login)
    @PostMapping("/login")
    public String login(User user, HttpSession session, Model model) {
        User loginUser = userService.findByUsername(user.getUsername());

        // 사용자 존재하고 비밀번호 일치하면 로그인 성공
        if (loginUser != null && loginUser.getPassword().equals(user.getPassword())) {
            session.setAttribute("loginUser", loginUser); // 세션에 로그인 정보 저장

            // 역할에 따라 이동 경로 다르게 지정
            if ("관리자".equals(loginUser.getRole())) {
                return "redirect:/admin"; // 관리자 페이지로 리다이렉트
            } else {
                return "redirect:/"; // 일반 메인 페이지로 리다이렉트
            }
        } else {
            model.addAttribute("errorMessage", "로그인 실패! 아이디 또는 비밀번호를 확인하세요.");
            return "login"; // 로그인 실패 시 다시 로그인 페이지
        }
    }

    // 로그아웃 처리 (GET /logout)
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화 → 로그아웃 처리
        return "redirect:/login"; // 로그인 페이지로 이동
    }
}

package com.example.admin_board.controller;

import com.example.admin_board.dto.Board;
import com.example.admin_board.dto.User;
import com.example.admin_board.repository.BoardRepository;
import com.example.admin_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // 관리자페이지 관련 기능 담당 컨트롤러

    // 사용자 관련 서비스
    @Autowired
    private UserService userService;

    // 게시판 관련 DB 처리
    @Autowired
    private BoardRepository boardRepository;

    // 관리자 메인 페이지 (회원 목록 + 게시판 목록 출력)
    @GetMapping
    public String adminPage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        // 관리자가 아니면 로그인 페이지로 이동
        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("username", loginUser.getUsername());

        // 전체 회원 목록 가져오기 + 최신 가입 순 정렬
        List<User> users = userService.findAllUsers();

        // 최신 가입 순으로 정렬
        users.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        model.addAttribute("users", users);

        // 전체 게시판 목록 가져오기
        List<Board> boards = boardRepository.findAll();
        model.addAttribute("boards", boards);

        return "admin";
    }


    // 회원관리: 회원 역할 수정
    @PostMapping("/users/update")
    public String updateUserRole(@RequestParam String username, @RequestParam String role, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        userService.updateUserRole(username, role);
        return "redirect:/admin";
    }

    // 회원관리: 회원 삭제
    @GetMapping("/users/delete/{username}")
    public String deleteUser(@PathVariable String username, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        userService.deleteUser(username);
        return "redirect:/admin";
    }

    // 게시판 추가
    @PostMapping("/boards/add")
    public String addBoard(@ModelAttribute Board board, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        boardRepository.save(board);
        return "redirect:/admin";
    }

    // 게시판 수정
    @PostMapping("/boards/update")
    public String updateBoard(@ModelAttribute Board board, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        boardRepository.update(board);

        model.addAttribute("message", "게시판이 수정되었습니다.");
        model.addAttribute("redirectUrl", "/admin");
        return "redirect:/admin";
    }

    // 게시판 삭제
    @GetMapping("/boards/delete/{id}")
    public String deleteBoard(@PathVariable int id, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"관리자".equals(loginUser.getRole())) {
            return "redirect:/login";
        }

        boardRepository.delete(id);
        return "redirect:/admin";
    }

}

package com.example.admin_board.controller;

import com.example.admin_board.dto.Board;
import com.example.admin_board.dto.Post;
import com.example.admin_board.dto.User;
import com.example.admin_board.repository.BoardRepository;
import com.example.admin_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    // 메인 페이지에 로그인 상태, 관리자 여부, 게시판 목록 정보를 제공하는 컨트롤러

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostService postService;

    // 홈(메인) 페이지 요청 처리
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // 세션에서 로그인된 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute("loginUser");

        // 로그인 여부 확인
        boolean isLogin = (loginUser != null);

        // 로그인 상태이고, 역할이 '관리자'인지 확인
        boolean isAdmin = isLogin && "관리자".equals(loginUser.getRole());

        // 로그인 여부와 관리자 여부를 뷰에 전달
        model.addAttribute("isLogin", isLogin);
        model.addAttribute("isAdmin", isAdmin);

        // 로그인 되어 있으면 사용자 이름도 뷰에 전달
        if (isLogin) {
            model.addAttribute("username", loginUser.getUsername());
        }
        // 모든 게시판 목록 조회
        List<Board> boards = boardRepository.findAll();

        // 각 게시판별 최근 게시글 5개씩 조회 후 Board 객체에 세팅
        for (Board board : boards) {
            List<Post> recentPosts = postService.findRecentPostsByBoardId(board.getId(), 5);
            board.setRecentPosts(recentPosts);
        }

        // 뷰에 boards 전달
        model.addAttribute("boards", boards);

        return "index";
    }


}

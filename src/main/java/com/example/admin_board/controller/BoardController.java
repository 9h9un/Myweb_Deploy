package com.example.admin_board.controller;

import com.example.admin_board.dto.Board;
import com.example.admin_board.dto.Post;
import com.example.admin_board.repository.BoardRepository;
import com.example.admin_board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BoardController {

    //	게시판에 등록된 글 목록 보기

    // 게시판 정보를 가져오기 위한 리포지토리
    @Autowired
    private BoardRepository boardRepository;

    // 게시글 목록을 가져오기 위한 서비스
    @Autowired
    private PostService postService;

    // 게시판 별 글 목록 조회
    @GetMapping("/boards/{boardId}")
    public String listPostsByBoard(@PathVariable int boardId, Model model) {
        // boardId로 해당 게시판 정보 조회
        Board board = boardRepository.findById(boardId);

        // 게시판이 존재하지 않으면 메인 페이지로 리다이렉트
        if (board == null) {
            return "redirect:/";
        }

        // 해당 게시판에 있는 게시글 목록 조회
        List<Post> posts = postService.findByBoardId(boardId);

        // 모든 게시판 목록 조회
        List<Board> boards = boardRepository.findAll();

        // 뷰에 전달할 데이터 설정
        model.addAttribute("boards", boards);    // 게시판 목록
        model.addAttribute("board", board);      // 게시판 정보
        model.addAttribute("posts", posts);      // 게시글 목록
        model.addAttribute("boardId", boardId);  // 게시판 ID

        // 게시판 글 목록 뷰
        return "board-post-list";
    }

}

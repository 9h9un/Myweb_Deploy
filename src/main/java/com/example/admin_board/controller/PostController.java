package com.example.admin_board.controller;

import com.example.admin_board.dto.Board;
import com.example.admin_board.dto.Post;
import com.example.admin_board.repository.BoardRepository;
import com.example.admin_board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    // 게시글 보기, 쓰기, 삭제 등 글 자체 관리

    @Autowired
    private PostService postService;

    @Autowired
    private BoardRepository boardRepository;

    // 게시글 상세보기
    @GetMapping("/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        Post post = postService.findById(postId);

        if (post == null) {
            // 없는 글 접근 시 처리 (예: 에러 페이지 또는 게시글 목록으로 리다이렉트)
            return "redirect:/posts";
        }

        // post의 boardId 기준으로 댓글 + 대댓글 모두 가져오기
        List<Post> comments = postService.findAllRepliesByBoardId(post.getBoardId());

        // 게시판 정보 조회 후 모델에 추가
        Board board = boardRepository.findById(post.getBoardId());

        model.addAttribute("post", post);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        return "post-detail";
    }

    // 새 글 쓰기 폼 (boardId 필요)
    @GetMapping("/new")
    public String newPostForm(@RequestParam int boardId, Model model) {
        Post post = new Post();
        post.setBoardId(boardId);
        model.addAttribute("post", post);

        Board board = boardRepository.findById(boardId);
        model.addAttribute("board", board);

        return "post-form";
    }

    // 답변 쓰기 폼 (parentPostId 필요)
    @GetMapping("/{postId}/reply")
    public String replyForm(@PathVariable Long postId, Model model) {
        Post reply = new Post();
        reply.setParentId(postId);
        Post original = postService.findById(postId);
        if (original != null) {
            reply.setBoardId(original.getBoardId());
        }
        model.addAttribute("post", reply);

        Board board = boardRepository.findById(reply.getBoardId());
        model.addAttribute("board", board);

        return "post-form";
    }

    // 글/답변 저장 처리
    @PostMapping("/save")
    public String savePost(Post post) {
        if (post.getParentId() != null) {
            if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
                Post parentPost = postService.findById(post.getParentId());
                if (parentPost != null) {
                    post.setTitle("[RE] " + parentPost.getTitle());
                } else {
                    post.setTitle("[RE]");
                }
            }
        }
        postService.save(post);

        Long redirectPostId;
        if (post.getParentId() != null) {
            Post parentPost = postService.findById(post.getParentId());
            Long rootPostId = postService.findRootPostId(parentPost);
            redirectPostId = (rootPostId != null) ? rootPostId : post.getId();
        } else {
            redirectPostId = post.getId();
        }

        // 저장 후 리다이렉션 대상 결정
        return "redirect:/posts/" + redirectPostId;
    }

    // 댓글 삭제 처리
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        if (post != null) {
            postService.delete(postId);
            // 삭제 후 원글 상세페이지로 이동 (댓글 삭제니까 원글 ID 필요)
            Long rootPostId = (post.getParentId() != null)
                    ? postService.findRootPostId(post)
                    : post.getId();
            return "redirect:/posts/" + rootPostId;
        }
        // 없는 글 삭제 시 메인 페이지 등으로 리다이렉트
        return "redirect:/";
    }

    // 게시글 삭제 처리
    @PostMapping("/{postId}/deletePost")
    public String deletePostMain(@PathVariable Long postId) {
        Post post = postService.findById(postId);
        if (post != null) {
            postService.delete(postId);
            // 게시글 삭제 후 게시판 목록으로 리다이렉트 (boardId 필요)
            return "redirect:/boards/" + post.getBoardId();
        }
        return "redirect:/";
    }


}

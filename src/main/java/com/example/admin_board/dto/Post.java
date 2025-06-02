package com.example.admin_board.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Post {
    private Long id;             // 게시글 고유 ID
    private int boardId;         // 게시판 ID (어느 게시판에 속하는지)
    private Long parentId;       // 답글이면 원글 ID, 아니면 null
    private String author;       // 작성자 아이디
    private String title;        // 게시글 제목
    private String content;      // 게시글 내용
    private LocalDateTime createdAt; // 작성일자

    // 기본 생성자
    public Post() {}

    // Getter, Setter 메서드들
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getBoardId() { return boardId; }
    public void setBoardId(int boardId) { this.boardId = boardId; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    // 답변(Post)들을 담는 리스트 - 계층형 댓글 구조 지원
    private List<Post> replies = new ArrayList<>();

    public List<Post> getReplies() {
        return replies;
    }

    public void setReplies(List<Post> replies) {
        this.replies = replies;
    }
}

package com.example.admin_board.dto;

import java.util.List;

public class Board {
    private int id;             // 게시판 고유 ID
    private String name;        // 게시판 이름
    private String description; // 게시판 설명
    private List<Post> recentPosts;  // 최근 게시글 목록 추가

    // 기본 생성자
    public Board() {}

    // 모든 필드를 포함한 생성자
    public Board(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getter, Setter 메서드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Post> getRecentPosts() {
        return recentPosts;
    }

    public void setRecentPosts(List<Post> recentPosts) {
        this.recentPosts = recentPosts;
    }
}

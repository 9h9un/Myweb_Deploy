package com.example.admin_board.dto;

import java.time.LocalDateTime;

public class User {
    private String username;          // 사용자 이름 저장
    private String password;          // 사용자 비밀번호 저장
    private String role;              // 사용자 권한(역할) 저장
    private LocalDateTime createdAt; // 생성 시간 저장

    public User() {}                 // 기본 생성자

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now(); // 객체 생성 시 현재 시간 저장
    }

    // 각 필드에 대한 getter와 setter 메서드
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

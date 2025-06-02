package com.example.admin_board.service;

import com.example.admin_board.dto.User;
import com.example.admin_board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 회원가입 처리 (아이디 중복 체크 후 저장)
    public boolean register(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return false; // 중복 아이디 있으면 가입 실패
        }
        userRepository.save(user);
        return true; // 가입 성공
    }

    // 로그인 처리 (아이디와 비밀번호 일치 여부 확인)
    public boolean login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            return user.getPassword().equals(password);
        } catch (Exception e) {
            return false; // 사용자 없거나 에러 시 로그인 실패
        }
    }

    // 아이디로 사용자 조회
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 모든 회원 조회
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // 회원 역할(role) 수정
    public boolean updateUserRole(String username, String role) {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setRole(role);
                userRepository.save(user);
                return true; // 역할 수정 성공
            }
            return false; // 사용자 없으면 실패
        } catch (Exception e) {
            return false; // 예외 발생 시 실패
        }
    }

    // 회원 삭제
    public boolean deleteUser(String username) {
        try {
            userRepository.deleteByUsername(username);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 예외 발생 시 실패
        }
    }
}

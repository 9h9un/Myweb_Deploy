package com.example.admin_board.repository;

import com.example.admin_board.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    // CRUD 기반 회원관리

    @Autowired
    private JdbcTemplate jdbcTemplate; // DB 작업용 JdbcTemplate 주입

    public void save(User user) {
        // 사용자명으로 기존 데이터 존재 여부 확인
        String checkSql = "SELECT COUNT(*) FROM user WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, user.getUsername());

        if (count != null && count > 0) {
            // 존재하면 회원 정보 업데이트
            String updateSql = "UPDATE user SET password = ?, role = ? WHERE username = ?";
            jdbcTemplate.update(updateSql, user.getPassword(), user.getRole(), user.getUsername());
        } else {
            // 없으면 새 회원 정보 삽입
            String insertSql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, user.getUsername(), user.getPassword(), user.getRole());
        }
    }

    // 사용자명으로 회원 조회
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        var users = jdbcTemplate.query(sql, new Object[]{username}, (rs, rowNum) ->
                new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                )
        );
        // 결과 없으면 null, 있으면 첫 번째 회원 반환
        return users.isEmpty() ? null : users.get(0);
    }

    // 모든 회원 조회
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                )
        );
    }

    // 사용자명으로 회원 삭제
    public void deleteByUsername(String username) {
        String sql = "DELETE FROM user WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }
}

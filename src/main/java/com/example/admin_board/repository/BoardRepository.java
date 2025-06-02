package com.example.admin_board.repository;

import com.example.admin_board.dto.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository {

    // 게시판 목록(board) 관련 CRUD

    // Spring에서 제공하는 JDBC 템플릿 객체를 자동 주입받음
    @Autowired
    private JdbcTemplate jdbc;

    // 모든 게시판 목록 조회
    public List<Board> findAll() {
        return jdbc.query("SELECT * FROM board", (rs, rowNum) ->
                new Board(rs.getInt("id"), rs.getString("name"), rs.getString("description")));
    }

    // 게시판 등록 (INSERT)
    public void save(Board board) {
        jdbc.update("INSERT INTO board (name, description) VALUES (?, ?)",
                board.getName(), board.getDescription());
    }

    // 게시판 수정 (UPDATE)
    public void update(Board board) {
        jdbc.update("UPDATE board SET name = ?, description = ? WHERE id = ?",
                board.getName(), board.getDescription(), board.getId());
    }

    // 게시판 삭제 (DELETE)
    public void delete(int id) {
        jdbc.update("DELETE FROM board WHERE id = ?", id);
    }

    // 특정 게시판 조회 (SELECT by ID)
    public Board findById(int id) {
        return jdbc.queryForObject("SELECT * FROM board WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> new Board(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
    }

}


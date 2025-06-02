package com.example.admin_board.repository;

import com.example.admin_board.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PostRepository {

    // 게시물 글, 댓글(Post) 관련 CRUD

    @Autowired
    private JdbcTemplate jdbc; // Spring JDBC 템플릿 객체, SQL 실행을 도와줌


    // 게시물 전체 조회 (id 오름차순 정렬)
    public List<Post> findAll() {
        return jdbc.query("SELECT * FROM post ORDER BY id ASC", (rs, rowNum) -> {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setBoardId(rs.getInt("board_id"));

            // parent_id가 null일 수 있으므로 null 체크
            long parentId = rs.getLong("parent_id");
            if (rs.wasNull()) {
                post.setParentId(null);
            } else {
                post.setParentId(parentId);
            }

            post.setAuthor(rs.getString("author"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));

            // 작성일 가져오기 (nullable 처리)
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                post.setCreatedAt(createdAt.toLocalDateTime());
            }

            return post;
        });
    }


    // 게시물 저장 (INSERT) - 입력 후 자동 생성된 ID(primary key)를 Post 객체에 설정
    public void save(Post post) {
        String sql = "INSERT INTO post (board_id, parent_id, author, title, content) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder(); // 자동 생성 키 저장 객체

        jdbc.update(connection -> {
            // SQL 실행 시 PreparedStatement로 파라미터 바인딩
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, post.getBoardId());

            // parent_id가 null이면 NULL 처리
            if (post.getParentId() != null) {
                ps.setLong(2, post.getParentId());
            } else {
                ps.setNull(2, java.sql.Types.BIGINT);
            }

            ps.setString(3, post.getAuthor());
            ps.setString(4, post.getTitle());
            ps.setString(5, post.getContent());

            return ps;
        }, keyHolder);

        // 생성된 ID를 Post 객체에 설정
        Number key = keyHolder.getKey();
        if (key != null) {
            post.setId(key.longValue());
        }
    }


    // 게시물 수정 (UPDATE)
    public void update(Post post) {
        jdbc.update(
                "UPDATE post SET board_id = ?, parent_id = ?, author = ?, title = ?, content = ? WHERE id = ?",
                post.getBoardId(),
                post.getParentId(),
                post.getAuthor(),
                post.getTitle(),
                post.getContent(),
                post.getId()
        );
    }


    // 게시물 삭제 (DELETE by ID)
    public void delete(long id) {
        jdbc.update("DELETE FROM post WHERE id = ?", id);
    }


    // 게시물 단건 조회 (SELECT by ID)
    public Post findById(long id) {
        return jdbc.queryForObject("SELECT * FROM post WHERE id = ?", new Object[]{id}, (rs, rowNum) -> {
            Post post = new Post();
            post.setId(rs.getLong("id"));
            post.setBoardId(rs.getInt("board_id"));

            long parentId = rs.getLong("parent_id");
            if (rs.wasNull()) {
                post.setParentId(null);
            } else {
                post.setParentId(parentId);
            }

            post.setAuthor(rs.getString("author"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                post.setCreatedAt(createdAt.toLocalDateTime());
            }

            return post;
        });
    }
}

package com.example.admin_board.service;

import com.example.admin_board.dto.Post;
import com.example.admin_board.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 새 글 또는 답글 저장
    public void save(Post post) {
        // 무한 루프 방지: 자기 자신을 부모로 등록하면 예외 발생
        if (post.getId() != null && post.getId().equals(post.getParentId())) {
            throw new IllegalArgumentException("자기 자신을 부모로 설정할 수 없습니다.");
        }

        // 순환 참조 방지
        if (isCircular(post)) {
            throw new IllegalArgumentException("순환 참조가 감지되었습니다.");
        }

        postRepository.save(post);
    }

    private boolean isCircular(Post post) {
        Long parentId = post.getParentId();
        while (parentId != null) {
            if (parentId.equals(post.getId())) {
                return true; // 자기가 조상 중에 있음 → 순환
            }
            Post parent = findById(parentId);
            if (parent == null) break;
            parentId = parent.getParentId();
        }
        return false;
    }

    // 특정 게시판의 원글(부모글, parentId가 null인 글) 리스트 조회 후 작성일 최신순 정렬
    public List<Post> findByBoardId(int boardId) {
        return postRepository.findAll().stream()
                .filter(p -> p.getBoardId() == boardId && p.getParentId() == null)
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())  // 작성일 기준 내림차순 정렬
                .toList();
    }


    // ID로 단일 게시글 조회
    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    // 특정 게시판의 댓글 및 대댓글(부모글이 있는 글, parentId가 null 아님) 모두 조회
    public List<Post> findAllRepliesByBoardId(int boardId) {
        return postRepository.findAll().stream()
                .filter(p -> p.getBoardId() == boardId && p.getParentId() != null)
                .toList();
    }

    // 댓글이나 대댓글이 주어졌을 때 최상위 원글(루트 게시글)의 ID 찾기
    public Long findRootPostId(Post post) {
        while (post.getParentId() != null) {
            post = findById(post.getParentId());
            if (post == null) {
                break;  // 부모글 없으면 종료
            }
        }
        return (post != null) ? post.getId() : null;
    }

    // 재귀 삭제 메서드로 교체
    public void delete(Long id) {
        List<Post> allPosts = postRepository.findAll();
        deleteRecursively(id, allPosts);
    }

    private void deleteRecursively(Long id, List<Post> allPosts) {
        for (Post child : allPosts) {
            if (id.equals(child.getParentId())) {
                deleteRecursively(child.getId(), allPosts); // 자식부터 삭제
            }
        }
        postRepository.delete(id); // 마지막에 자신 삭제
    }

    // 각 게시판별로 최근 게시글 가져오는 메서드
    public List<Post> findRecentPostsByBoardId(int boardId, int limit) {
        return postRepository.findAll().stream()
                .filter(p -> p.getBoardId() == boardId && p.getParentId() == null)
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}

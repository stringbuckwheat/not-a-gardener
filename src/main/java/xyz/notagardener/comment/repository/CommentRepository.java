package xyz.notagardener.comment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.notagardener.comment.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"gardener"})
    List<Comment> findByPost_Id(Long postId, Pageable pageable);

    @EntityGraph(attributePaths = {"gardener"})
    Optional<Comment> findById(Long id);
}

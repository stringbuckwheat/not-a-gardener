package xyz.notagardener.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.notagardener.post.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostQueryRepository {
    @EntityGraph(attributePaths = {"gardener", "images"})
    List<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"images", "gardener"})
    Optional<Post> findById(Long id);
}

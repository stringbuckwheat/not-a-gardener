package xyz.notagardener.like.repostiory;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.notagardener.like.entity.Like;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByGardener_GardenerIdAndPost_Id(Long gardenerId, Long postId);
    Long countByPost_Id(Long postId);

    @EntityGraph(attributePaths = {"gardener", "post", "post.images"})
    List<Like> findByGardener_GardenerIdOrderByCreatedDateDesc(Long gardenerId);
}

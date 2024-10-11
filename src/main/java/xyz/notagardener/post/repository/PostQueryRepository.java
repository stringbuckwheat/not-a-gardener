package xyz.notagardener.post.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.post.dto.PostResponse;
import xyz.notagardener.post.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostQueryRepository {
    List<PostResponse> getAllBy(Pageable pageable, Long gardenerId);
    List<Post> getOverviews(Pageable pageable, String username);
    Optional<Gardener> getGardener(Long gardenerId);
}

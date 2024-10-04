package xyz.notagardener.post.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.post.dto.PostResponse;
import xyz.notagardener.post.model.Post;

import java.util.List;

public interface PostQueryRepository {
    List<PostResponse> getAllBy(Pageable pageable, Long gardenerId);
    List<Post> getOverviews(Pageable pageable, String username);
}

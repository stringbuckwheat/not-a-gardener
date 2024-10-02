package xyz.notagardener.post.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.post.dto.PostResponse;

import java.util.List;

public interface PostQueryRepository {
    List<PostResponse> getAllBy(Pageable pageable, Long gardenerId);
}

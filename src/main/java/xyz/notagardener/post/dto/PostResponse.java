package xyz.notagardener.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.dto.LikeResponse;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {
    private Long postId;
    private String content;
    private List<ImageResponse> images;
    private LocalDateTime createdAt;

    // 유저 정보
    private Long gardenerId;
    private String name;

    private LikeResponse like;

    @QueryProjection
    public PostResponse(Post post, Long likeCount, boolean isLiked) {
        this(post);
        this.like = new LikeResponse(likeCount, isLiked);
    }

    public PostResponse(Post post, LikeResponse like) {
        this(post);
        this.like = like;
    }

    private PostResponse(Post post) {
        this.postId = post.getId();
        this.content = post.getContent();
        this.images = post.getImages().stream().map(ImageResponse::new).toList();
        this.createdAt = post.getCreatedDate();

        Gardener gardener = post.getGardener();
        this.gardenerId = gardener.getGardenerId();
        this.name = gardener.getName();
    }
}

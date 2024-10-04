package xyz.notagardener.post.dto;

import lombok.Getter;
import xyz.notagardener.post.model.Post;

@Getter
public class PostOverview {
    private Long postId;
    private ImageResponse cover;

    public PostOverview(Post post) {
        this.postId = post.getId();
        this.cover = new ImageResponse(post.getImages().get(0));
    }
}

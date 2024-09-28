package xyz.notagardener.post.dto;

import lombok.Getter;
import xyz.notagardener.post.model.PostImage;

@Getter
public class ImageResponse {
    private Long id;
    private String url;

    public ImageResponse(PostImage image) {
        this.id = image.getId();
        this.url = image.getUrl();
    }
}

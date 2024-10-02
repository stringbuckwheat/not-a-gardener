package xyz.notagardener.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private Long likeCount;
    private boolean isLiked;
}

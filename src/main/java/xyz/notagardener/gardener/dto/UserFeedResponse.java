package xyz.notagardener.gardener.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserFeedResponse {
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
    private Long plantCount;

    @QueryProjection
    public UserFeedResponse(Long postCount, Long followerCount, Long followingCount, Long plantCount) {
        this.postCount = postCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.plantCount = plantCount;
    }
}

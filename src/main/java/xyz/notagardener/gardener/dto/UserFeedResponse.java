package xyz.notagardener.gardener.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.gardener.model.Gardener;

@Getter
@NoArgsConstructor
public class UserFeedResponse {
    private SocialGardenerInfo socialGardenerInfo;
    private Long postCount;
    private Long followerCount;
    private Long followingCount;
    private Long plantCount;

    private String biography;
    private boolean isFollowingMe;

    @QueryProjection
    public UserFeedResponse(Gardener gardener, Long postCount, Long followerCount, Long followingCount, Long plantCount, boolean isFollowingMe) {
        this.socialGardenerInfo = new SocialGardenerInfo(gardener);
        this.biography = gardener.getBiography();
        this.postCount = postCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.plantCount = plantCount;
        this.isFollowingMe = isFollowingMe;
    }
}

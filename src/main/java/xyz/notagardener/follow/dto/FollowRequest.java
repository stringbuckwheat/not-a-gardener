package xyz.notagardener.follow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class FollowRequest {
    @NotNull
    private Long followerId; // 팔로우 한 사람

    @NotNull
    private Long followingId; // 팔로우 당한 사람
}

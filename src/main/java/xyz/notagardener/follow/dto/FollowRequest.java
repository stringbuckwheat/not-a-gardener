package xyz.notagardener.follow.dto;

import lombok.Getter;

@Getter
public class FollowRequest {
    private Long followerId; // 팔로우 한 사람
    private Long followingId; // 팔로우 당한 사람
}

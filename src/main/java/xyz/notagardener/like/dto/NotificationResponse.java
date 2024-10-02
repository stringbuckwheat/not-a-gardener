package xyz.notagardener.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import xyz.notagardener.like.entity.Like;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class NotificationResponse {
    private String type; // TODO enum으로 분리 - 댓글, 좋아요, 팔로우 등

    private String profileImageUrl;
    private String gardenerName;
    private String username;

    private Long postId;
    private String postImageUrl;

    private Long id; // 댓글, 좋아요, 팔로우 id
    private LocalDateTime createDate;
    private LocalDateTime readDate;

    public NotificationResponse(Like like) {
        this.type = "LIKE";
        this.profileImageUrl = like.getGardener().getProfileImageUrl();
        this.gardenerName = like.getGardener().getName();
        this.username = like.getGardener().getUsername();
        this.postId = like.getPost().getId();
        this.postImageUrl = like.getPost().getImages().get(0).getUrl();
        this.id = like.getId();
        this.createDate = like.getCreatedDate();
        this.readDate = like.getReadDate();
    }
}

package xyz.notagardener.notification.dto;

import lombok.Getter;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.notification.enums.NotificationType;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private Long targetId;          // 알림 보낼 ID
    private NotificationType type;  // 알림 타입

    private String profileImageUrl; // 프로필 이미지 URL
    private String gardenerName;     // 가드너 이름
    private String username;         // 사용자 이름

    private Long postId;            // 포스트 ID
    private String postImageUrl;    // 포스트 이미지 URL
    private String content;          // 내용
    private LocalDateTime createDate; // 생성 날짜

    public NotificationResponse(Notifiable notifiable) {
        this.targetId = notifiable.getId();
        this.createDate = notifiable.getCreatedDate();
        Gardener gardener = notifiable.getGardener();
        this.profileImageUrl = gardener.getProfileImageUrl();
        this.gardenerName = gardener.getName();
        this.username = gardener.getUsername();

        // 알림 타입에 따라 추가 데이터 설정
        if (notifiable instanceof Like like) {
            this.type = NotificationType.LIKE;
            Post post = like.getPost();
            this.postId = post != null ? post.getId() : null;
            this.postImageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getUrl();
        } else if (notifiable instanceof Comment comment) {
            this.type = NotificationType.COMMENT;
            this.postId = comment.getPost() != null ? comment.getPost().getId() : null;
            this.content = comment.getContent();
        } else if (notifiable instanceof Follow) {
            this.type = NotificationType.FOLLOW;
        } else if (notifiable instanceof Post post) {
            this.type = NotificationType.POST;
            this.postId = post.getId();
            this.content = post.getContent();
            this.postImageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getUrl();
        }
    }
}

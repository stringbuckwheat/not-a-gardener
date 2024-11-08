package xyz.notagardener.notification.dto;

import lombok.Getter;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.notification.enums.NotificationType;
import xyz.notagardener.notification.model.Notification;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private Long targetId;          // 알림 보낼 ID
    private String content;          // 내용
    private NotificationType type;  // 알림 타입
    private LocalDateTime createDate; // 생성 날짜

    private String profileImageUrl; // 프로필 이미지 URL
    private String gardenerName;     // 가드너 이름
    private String username;         // 사용자 이름

    private Long postId;            // 포스트 ID
    private String postImageUrl;    // 포스트 이미지 URL

    public NotificationResponse(Notification notification) {
        this.targetId = notification.getNotifiable().getId();
        this.content = notification.getNotifiable().getContent();
        this.type = notification.getType();
        this.createDate = notification.getNotifiable().getCreatedDate();

        Gardener gardener = notification.getNotifiable().getGardener();
        this.profileImageUrl = gardener.getProfileImageUrl();
        this.gardenerName = gardener.getName();
        this.username = gardener.getUsername();

        setPostData(getPost(notification));
    }

    private Post getPost(Notification notification) {
        return switch (type) {
            case LIKE -> notification.getLike().getPost();
            case COMMENT -> notification.getComment().getPost();
            case POST -> notification.getPost();
            default -> null;
        };
    }

    private void setPostData(Post post) {
        if (post == null) {
            return;
        }

        this.postId = post.getId();
        this.postImageUrl = post.getImages().isEmpty() ? null : post.getImages().get(0).getUrl();
    }
}

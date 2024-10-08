package xyz.notagardener.common.notification.dto;

import lombok.Getter;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.common.notification.enums.NotificationType;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Getter
public class DefaultNotification extends Notification {
    private String profileImageUrl;
    private String gardenerName;
    private String username;
    private Long postId;
    private String postImageUrl;
    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime readDate;

    public DefaultNotification(Like like) {
        this(NotificationType.LIKE, like);
    }

    public DefaultNotification(Comment comment) {
        this(NotificationType.COMMENT, comment);
    }

    private DefaultNotification(NotificationType type, Like like) {
        super(type);
        setCommonFields(like.getGardener(), like.getPost(), like.getId(), like.getCreatedDate(), like.getReadDate());
    }

    private DefaultNotification(NotificationType type, Comment comment) {
        super(type);
        this.content = comment.getContent();
        setCommonFields(comment.getGardener(), comment.getPost(), comment.getId(), comment.getCreatedDate(), comment.getReadDate());
    }

    private void setCommonFields(Gardener gardener, Post post, Long id, LocalDateTime createdDate, LocalDateTime readDate) {
        this.profileImageUrl = gardener.getProfileImageUrl();
        this.gardenerName = gardener.getName();
        this.username = gardener.getUsername();
        this.postId = post.getId();
        this.postImageUrl = post.getImages().get(0).getUrl();
        this.id = id;
        this.createDate = createdDate;
        this.readDate = readDate;
    }
}


package xyz.notagardener.notification.dto;

import lombok.Getter;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private String type;
    private String profileImageUrl;
    private String gardenerName;
    private String username;
    private Long postId;
    private String postImageUrl;
    private Long id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime readDate;

    public NotificationResponse(Like like) {
        this("LIKE", like);
    }

    public NotificationResponse(Comment comment) {
        this("COMMENT", comment);
    }

    private NotificationResponse(String type, Like like) {
        this.type = type;
        setCommonFields(like.getGardener(), like.getPost(), like.getId(), like.getCreatedDate(), like.getReadDate());
    }

    private NotificationResponse(String type, Comment comment) {
        this.type = type;
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


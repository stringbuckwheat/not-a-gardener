package xyz.notagardener.notification.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.comment.model.Comment;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;
import xyz.notagardener.notification.enums.NotificationType;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Follow follow;

    @ManyToOne(fetch = FetchType.LAZY)
    private Like like;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    private Gardener gardener;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate; // 생성 일자

    private LocalDateTime readDate;

    public Notification(Gardener gardener, NotificationType type, Comment comment, Follow follow, Like like, Post post) {
        this.gardener = gardener;
        this.type = type;
        this.comment = comment;
        this.follow = follow;
        this.like = like;
        this.post = post;
    }

    public Notifiable getNotifiable() {
        return switch (this.getType()) {
            case COMMENT -> comment;
            case FOLLOW -> follow;
            case LIKE -> like;
            case POST -> post;
        };
    }

    public void read() {
        this.readDate = LocalDateTime.now();
    }
}


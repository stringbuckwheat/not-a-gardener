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

    public Notification(Notifiable notifiable) {
        this.gardener = notifiable.getGardener(); // Gardener를 설정
        setData(notifiable);
    }

    private void setData(Notifiable notifiable) {
        if (notifiable instanceof Comment comment) {
            this.type = NotificationType.COMMENT;
            this.comment = comment;
        } else if (notifiable instanceof Follow follow) {
            this.type = NotificationType.FOLLOW;
            this.follow = follow;
        } else if (notifiable instanceof Like like) {
            this.type = NotificationType.LIKE;
            this.like = like;
        } else if (notifiable instanceof Post post) {
            this.type = NotificationType.POST;
            this.post = post;
        }
    }

    public void read() {
        this.readDate = LocalDateTime.now();
    }
}


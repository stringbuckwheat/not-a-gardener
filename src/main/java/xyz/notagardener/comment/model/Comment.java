package xyz.notagardener.comment.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    private Gardener gardener;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate; // 생성 일자

    private LocalDateTime deletedDate; // 논리 삭제

    private LocalDateTime readDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();

    @Builder
    private Comment(String content, Gardener gardener, Post post, Comment parent) {
        this.content = content;
        this.gardener = gardener;
        this.post = post;
        this.parent = parent;
    }

    public void delete() {
        this.deletedDate = LocalDateTime.now();
    }
    public void readNotification() {
        this.readDate = LocalDateTime.now();
    }
}

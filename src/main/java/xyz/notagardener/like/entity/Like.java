package xyz.notagardener.like.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_like", uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "gardener_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Like implements Notifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "gardener_id")
    private Gardener gardener;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    public Like(Post post, Gardener gardener) {
        this.post = post;
        this.gardener = gardener;
    }
}

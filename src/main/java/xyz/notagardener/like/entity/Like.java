package xyz.notagardener.like.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.common.model.BaseEntity;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.post.model.Post;

@Entity
@Table(name = "post_like", uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "gardener_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Like extends BaseEntity {
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

    public Like(Post post, Gardener gardener) {
        this.post = post;
        this.gardener = gardener;
    }
}

package xyz.notagardener.post.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.common.model.BaseEntity;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.like.entity.Like;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Like> likes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    public Post(String content, Gardener gardener) {
        this.content = content;
        this.gardener = gardener;
    }
}

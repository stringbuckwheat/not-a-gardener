package xyz.notagardener.follow.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.common.model.Notifiable;
import xyz.notagardener.gardener.model.Gardener;

import java.time.LocalDateTime;

@Entity
@Table(name = "gardener_follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Follow implements Notifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Gardener follower;  // 팔로우 한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private Gardener following;  // 팔로우 당한 사람

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdDate;

    @Builder
    public Follow(Gardener follower, Gardener following) {
        this.follower = follower;
        this.following = following;
    }

    public Long getFollowerId() {
        return follower.getGardenerId();
    }

    @Override
    public Gardener getGardener() {
        return follower;
    }

    @Override
    public String getContent() {
        return null;
    }
}

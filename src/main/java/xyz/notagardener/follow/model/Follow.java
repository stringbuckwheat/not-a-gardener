package xyz.notagardener.follow.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.gardener.model.Gardener;

import java.time.LocalDateTime;

@Entity
@Table(name = "gardener_follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Gardener follower;  // 팔로우 한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private Gardener following;  // 팔로우 당한 사람

    private LocalDateTime followDate;  // 팔로우 시간

    @Builder
    public Follow(Gardener follower, Gardener following) {
        this.follower = follower;
        this.following = following;
        this.followDate = LocalDateTime.now();
    }
}
package xyz.notagardener.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.notagardener.follow.model.Follow;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollower_GardenerIdAndFollowing_GardenerId(Long followerId, Long followingId);
}

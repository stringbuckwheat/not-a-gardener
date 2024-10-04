package xyz.notagardener.gardener.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xyz.notagardener.gardener.dto.QUserFeedResponse;
import xyz.notagardener.gardener.dto.UserFeedResponse;

import static xyz.notagardener.follow.model.QFollow.follow;
import static xyz.notagardener.gardener.model.QGardener.gardener;
import static xyz.notagardener.plant.model.QPlant.plant;
import static xyz.notagardener.post.model.QPost.post;

@Repository
@RequiredArgsConstructor
public class FeedRepository {
    private final JPAQueryFactory queryFactory;

    public UserFeedResponse getFeed(String username) {
        return queryFactory
                .select(
                        new QUserFeedResponse(
                                post.count(),
                                follow.follower.count(),
                                follow.following.count(),
                                plant.count()
                        )
                )
                .from(gardener)
                .leftJoin(gardener.followers, follow)
                .leftJoin(gardener.posts, post)
                .leftJoin(gardener.plants, plant)
                .where(gardener.username.eq(username))
                .groupBy(gardener)
                .fetchOne();
    }
}

package xyz.notagardener.gardener.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xyz.notagardener.gardener.dto.QSocialGardenerInfo;
import xyz.notagardener.gardener.dto.QUserFeedResponse;
import xyz.notagardener.gardener.dto.SocialGardenerInfo;
import xyz.notagardener.gardener.dto.UserFeedResponse;

import java.util.List;

import static xyz.notagardener.follow.model.QFollow.follow;
import static xyz.notagardener.gardener.model.QGardener.gardener;
import static xyz.notagardener.plant.model.QPlant.plant;
import static xyz.notagardener.post.model.QPost.post;

@Repository
@RequiredArgsConstructor
public class SocialRepository {
    private final JPAQueryFactory queryFactory;

    public UserFeedResponse getProfile(String username, Long myId) {
        return queryFactory
                .select(
                        new QUserFeedResponse(
                                gardener,
                                post.count(),
                                follow.follower.count(),
                                follow.following.count(),
                                plant.count(),
                                JPAExpressions.selectFrom(follow)
                                        .where(follow.follower.username.eq(username)
                                                .and(follow.following.gardenerId.eq(myId)))
                                        .exists()
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

    public List<SocialGardenerInfo> getTagInfoBy(String query, Long myId) {
        // 1) 내가 팔로잉 하거나 나를 팔로우 하는 사람을 우선 정렬
        // 2) 그렇지 않은 사람
        OrderSpecifier<Integer> orderByExpression = new CaseBuilder()
                .when(follow.follower.gardenerId.eq(myId)
                        .or(follow.following.gardenerId.eq(myId))).then(0)
                .otherwise(1)
                .asc();

        return queryFactory
                .select(new QSocialGardenerInfo(gardener))
                .from(gardener)
                .leftJoin(gardener.followers, follow)
                .leftJoin(gardener.followings, follow)
                .where(gardener.username.like(query))
                .orderBy(orderByExpression)
                .fetch();
    }

    public List<SocialGardenerInfo> getTagInfoBy(Long myId) {
        return queryFactory
                .select(new QSocialGardenerInfo(gardener))
                .from(gardener)
                .where(
                        follow.follower.gardenerId.eq(myId)
                                .or(follow.following.gardenerId.eq(myId))
                )
                .orderBy(gardener.username.asc())
                .fetch();
    }
}

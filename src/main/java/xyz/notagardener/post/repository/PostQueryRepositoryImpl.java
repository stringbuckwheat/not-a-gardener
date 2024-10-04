package xyz.notagardener.post.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import xyz.notagardener.post.dto.PostOverview;
import xyz.notagardener.post.dto.PostResponse;
import xyz.notagardener.post.dto.QPostResponse;
import xyz.notagardener.post.model.Post;

import java.util.List;
import java.util.stream.Collectors;

import static xyz.notagardener.gardener.model.QGardener.gardener;
import static xyz.notagardener.like.entity.QLike.like;
import static xyz.notagardener.post.model.QPost.post;
import static xyz.notagardener.post.model.QPostImage.postImage;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<PostResponse> getAllBy(Pageable pageable, Long gardenerId) {
        return queryFactory
                .select(
                        new QPostResponse(
                                post,
                                like.count(),
                                JPAExpressions.selectFrom(like)
                                        .where(like.post.eq(post)
                                                .and(like.gardener.gardenerId.eq(gardenerId)))
                                        .exists()  // 현재 사용자가 좋아요를 눌렀는지 확인
                        )
                )
                .from(post)
                .join(post.gardener, gardener)
                .fetchJoin()
                .leftJoin(post.likes, like)
                .groupBy(post.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<Post> getOverviews(Pageable pageable, String username) {
        return queryFactory
                .select(post)
                .from(post)
                .join(post.gardener, gardener)
                .leftJoin(post.images, postImage)
                .fetchJoin()
                .where(gardener.username.eq(username))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}

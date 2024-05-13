package xyz.notagardener.domain.gardener.repository;

import xyz.notagardener.domain.gardener.dto.GardenerDetail;
import xyz.notagardener.domain.gardener.dto.QGardenerDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.domain.gardener.QGardener;


@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GardenerRepositoryCustomImpl implements GardenerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GardenerDetail findGardenerDetailByGardenerId(Long gardenerId) {
        return queryFactory.select(
                        new QGardenerDetail(
                                QGardener.gardener.gardenerId,
                                QGardener.gardener.username,
                                QGardener.gardener.email,
                                QGardener.gardener.name,
                                QGardener.gardener.createDate,
                                QGardener.gardener.provider
                        )
                )
                .from(QGardener.gardener)
                .where(QGardener.gardener.gardenerId.eq(gardenerId))
                .fetchOne();
    }
}

package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.GardenerDetail;
import com.buckwheat.garden.gardener.gardener.QGardenerDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.buckwheat.garden.data.entity.QGardener.gardener;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GardenerRepositoryCustomImpl implements GardenerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public GardenerDetail findGardenerDetailByGardenerId(Long gardenerId) {
        return queryFactory.select(
                        new QGardenerDetail(
                                gardener.gardenerId,
                                gardener.username,
                                gardener.email,
                                gardener.name,
                                gardener.createDate,
                                gardener.provider
                        )
                )
                .from(gardener)
                .where(gardener.gardenerId.eq(gardenerId))
                .fetchOne();
    }
}

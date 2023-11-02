package com.buckwheat.garden.repository.impl;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.dto.gardener.QGardenerDetail;
import com.buckwheat.garden.repository.GardenerRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.buckwheat.garden.data.entity.QGardener.gardener;

@RequiredArgsConstructor
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

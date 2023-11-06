package com.buckwheat.garden.repository.query.querydsl.impl;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.query.querydsl.PlaceRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.buckwheat.garden.data.entity.QPlace.place;
import static com.buckwheat.garden.data.entity.QPlant.plant;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countPlantsByPlaceId(Long placeId) {
        return queryFactory
                .select(plant.count())
                .from(plant)
                .where(plant.place.placeId.eq(placeId))
                .fetchFirst();
    }

    @Override
    public List<Plant> findPlantsByPlaceIdWithPage(Long placeId, Pageable pageable) {
        return queryFactory
                .selectFrom(plant)
                .join(plant.place, place)
                .where(plant.place.placeId.eq(placeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(plant.createDate.desc())
                .fetch();
    }
}

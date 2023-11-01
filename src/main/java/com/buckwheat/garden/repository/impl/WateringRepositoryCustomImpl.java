package com.buckwheat.garden.repository.impl;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.WateringRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static com.buckwheat.garden.data.entity.QChemical.chemical;
import static com.buckwheat.garden.data.entity.QPlace.place;
import static com.buckwheat.garden.data.entity.QPlant.plant;
import static com.buckwheat.garden.data.entity.QWatering.watering;

@Slf4j
@RequiredArgsConstructor
public class WateringRepositoryCustomImpl implements WateringRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Watering> findAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate) {
        return queryFactory
                .selectFrom(watering)
                .leftJoin(watering.chemical, chemical)
                .fetchJoin() // LazyInitializationException
                .join(watering.plant, plant)
                .fetchJoin() // LazyInitializationException
                .where(
                        plant.gardener.gardenerId.eq(gardenerId)
                                .and(watering.wateringDate.after(startDate))
                                .and(watering.wateringDate.before(endDate))
                )
                .orderBy(watering.wateringDate.asc())
                .fetch();
    }

    @Override
    public List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable) {
        List<Watering> waterings = queryFactory
                .selectFrom(watering)
                .from(watering)
                .join(watering.plant, plant)
                .fetchJoin()
                .join(watering.plant.place, place)
                .fetchJoin()
                .join(watering.chemical, chemical)
                .fetchJoin()
                .where(watering.chemical.chemicalId.eq(chemicalId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(watering.wateringDate.desc())
                .fetch();

        return waterings;
    }
}

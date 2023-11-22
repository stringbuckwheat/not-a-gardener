package com.buckwheat.garden.domain.watering.repository;

import com.buckwheat.garden.domain.watering.Watering;
import com.buckwheat.garden.domain.watering.dto.ChemicalUsage;
import com.buckwheat.garden.domain.watering.dto.QChemicalUsage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.buckwheat.garden.domain.chemical.QChemical.chemical;
import static com.buckwheat.garden.domain.plant.QPlant.plant;
import static com.buckwheat.garden.domain.watering.QWatering.watering;
import static org.hibernate.internal.util.NullnessHelper.coalesce;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    public LocalDate findLatestWateringDate(Long plantId) {
        return queryFactory
                .select(watering.wateringDate.max())
                .from(watering)
                .where(watering.plant.plantId.eq(plantId))
                .fetchOne();
    }

    @Override
    public List<ChemicalUsage> findLatestChemicalizedDayList(Long gardenerId, Long plantId, String active) {
        return queryFactory
                .select(
                        new QChemicalUsage(
                                chemical.chemicalId,
                                chemical.period,
                                chemical.name,
                                coalesce(watering.wateringDate.max(), watering.wateringDate.min())
                        )
                )
                .from(chemical)
                .leftJoin(watering)
                .on(chemical.chemicalId.eq(watering.chemical.chemicalId).and(watering.plant.plantId.eq(plantId)))
                .where(chemical.gardener.gardenerId.eq(gardenerId).and(chemical.active.eq(active)))
                .groupBy(chemical.chemicalId, chemical.period, chemical.name)
                .orderBy(chemical.period.desc())
                .fetch();
    }

    @Override
    public Boolean existByWateringDateAndPlantId(LocalDate wateringDate, Long plantId) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(watering)
                .where(
                        watering.wateringDate.eq(wateringDate)
                                .and(
                                        watering.plant.plantId.eq(plantId)
                                )
                )
                .fetchFirst();

        return fetchOne != null;
    }

    public List<Watering> findLatestFourWateringDate(Long plantId) {
        return queryFactory.selectFrom(watering)
                .where(watering.plant.plantId.eq(plantId))
                .orderBy(watering.wateringDate.desc())
                .limit(4)
                .fetch();
    }

    @Override
    public List<Watering> findWateringsByPlantIdWithPage(Long plantId, Pageable pageable) {
        return queryFactory
                .selectFrom(watering)
                .leftJoin(watering.chemical, chemical)
                .fetchJoin()
                .where(watering.plant.plantId.eq(plantId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize() + 1) // 페이지 사이즈
                .orderBy(watering.wateringDate.desc())
                .fetch();
    }
}

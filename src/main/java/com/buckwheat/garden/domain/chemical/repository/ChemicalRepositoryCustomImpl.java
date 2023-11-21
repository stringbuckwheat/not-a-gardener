package com.buckwheat.garden.domain.chemical.repository;

import com.buckwheat.garden.domain.chemical.dto.ChemicalDto;
import com.buckwheat.garden.domain.chemical.dto.QChemicalDto;
import com.buckwheat.garden.domain.watering.Watering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.buckwheat.garden.domain.chemical.QChemical.chemical;
import static com.buckwheat.garden.domain.place.QPlace.place;
import static com.buckwheat.garden.domain.plant.QPlant.plant;
import static com.buckwheat.garden.domain.watering.QWatering.watering;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ChemicalRepositoryCustomImpl implements ChemicalRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChemicalDto> findAllChemicals(Long gardenerId) {
        return queryFactory.select(
                        new QChemicalDto(
                                chemical.chemicalId,
                                chemical.name,
                                chemical.type,
                                chemical.period
                        )
                )
                .from(chemical)
                .where(chemical.active.eq("Y")
                        .and(chemical.gardener.gardenerId.eq(gardenerId)))
                .fetch();
    }

    @Override
    public ChemicalDto findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId) {
        return queryFactory.select(
                        new QChemicalDto(
                                chemical.chemicalId,
                                chemical.name,
                                chemical.type,
                                chemical.period
                        )
                )
                .from(chemical)
                .where(chemical.active.eq("Y")
                        .and(chemical.gardener.gardenerId.eq(gardenerId))
                        .and(chemical.chemicalId.eq(chemicalId))
                )
                .fetchOne();
    }

    @Override
    public Long countWateringByChemicalId(Long chemicalId) {
        return queryFactory
                .select(watering.count())
                .from(watering)
                .where(watering.chemical.chemicalId.eq(chemicalId))
                .fetchFirst();
    }

    @Override
    public List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable) {
        return queryFactory
                .selectFrom(watering)
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
    }
}

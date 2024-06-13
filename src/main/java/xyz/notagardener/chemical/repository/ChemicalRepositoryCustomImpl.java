package xyz.notagardener.chemical.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.chemical.dto.ChemicalDto;
import xyz.notagardener.chemical.dto.QChemicalDto;
import xyz.notagardener.place.QPlace;
import xyz.notagardener.plant.QPlant;
import xyz.notagardener.watering.Watering;

import java.util.List;

import static xyz.notagardener.chemical.QChemical.chemical;
import static xyz.notagardener.watering.QWatering.watering;

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
                .join(watering.plant, QPlant.plant)
                .fetchJoin()
                .join(watering.plant.place, QPlace.place)
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

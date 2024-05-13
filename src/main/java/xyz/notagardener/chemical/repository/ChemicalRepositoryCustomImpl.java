package xyz.notagardener.domain.chemical.repository;

import xyz.notagardener.domain.chemical.dto.ChemicalDto;
import xyz.notagardener.domain.chemical.dto.QChemicalDto;
import xyz.notagardener.domain.watering.Watering;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.domain.chemical.QChemical;
import xyz.notagardener.domain.place.QPlace;
import xyz.notagardener.domain.plant.QPlant;
import xyz.notagardener.domain.watering.QWatering;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class ChemicalRepositoryCustomImpl implements ChemicalRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChemicalDto> findAllChemicals(Long gardenerId) {
        return queryFactory.select(
                        new QChemicalDto(
                                QChemical.chemical.chemicalId,
                                QChemical.chemical.name,
                                QChemical.chemical.type,
                                QChemical.chemical.period
                        )
                )
                .from(QChemical.chemical)
                .where(QChemical.chemical.active.eq("Y")
                        .and(QChemical.chemical.gardener.gardenerId.eq(gardenerId)))
                .fetch();
    }

    @Override
    public Optional<ChemicalDto> findByChemicalIdAndGardenerId(Long chemicalId, Long gardenerId) {
        ChemicalDto result = queryFactory.select(
                        new QChemicalDto(
                                QChemical.chemical.chemicalId,
                                QChemical.chemical.name,
                                QChemical.chemical.type,
                                QChemical.chemical.period
                        )
                )
                .from(QChemical.chemical)
                .where(QChemical.chemical.active.eq("Y")
                        .and(QChemical.chemical.gardener.gardenerId.eq(gardenerId))
                        .and(QChemical.chemical.chemicalId.eq(chemicalId))
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Long countWateringByChemicalId(Long chemicalId) {
        return queryFactory
                .select(QWatering.watering.count())
                .from(QWatering.watering)
                .where(QWatering.watering.chemical.chemicalId.eq(chemicalId))
                .fetchFirst();
    }

    @Override
    public List<Watering> findWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable) {
        return queryFactory
                .selectFrom(QWatering.watering)
                .join(QWatering.watering.plant, QPlant.plant)
                .fetchJoin()
                .join(QWatering.watering.plant.place, QPlace.place)
                .fetchJoin()
                .join(QWatering.watering.chemical, QChemical.chemical)
                .fetchJoin()
                .where(QWatering.watering.chemical.chemicalId.eq(chemicalId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(QWatering.watering.wateringDate.desc())
                .fetch();
    }
}

package com.buckwheat.garden.repository.querydsl.impl;

import com.buckwheat.garden.data.dto.chemical.ChemicalDto;
import com.buckwheat.garden.data.dto.chemical.QChemicalDto;
import com.buckwheat.garden.repository.querydsl.ChemicalRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.buckwheat.garden.data.entity.QChemical.chemical;

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
}

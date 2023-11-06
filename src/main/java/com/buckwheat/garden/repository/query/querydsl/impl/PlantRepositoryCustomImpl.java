package com.buckwheat.garden.repository.query.querydsl.impl;

import com.buckwheat.garden.data.dto.garden.QWaitingForWatering;
import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.query.querydsl.PlantRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.buckwheat.garden.data.entity.QPlace.place;
import static com.buckwheat.garden.data.entity.QPlant.plant;
import static com.buckwheat.garden.data.entity.QWatering.watering;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantRepositoryCustomImpl implements PlantRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean existByGardenerId(Long gardenerId) {
        Plant fetchOne = queryFactory
                .selectFrom(plant)
                .where(plant.gardener.gardenerId.eq(gardenerId))
                .fetchFirst();

        return fetchOne != null;
    }

    @Override
    public List<WaitingForWatering> findWaitingForWateringList(Long gardenerId) {
        return queryFactory
                .select(
                        new QWaitingForWatering(
                                plant.plantId,
                                plant.name,
                                plant.species,
                                plant.medium,
                                plant.place.placeId,
                                plant.place.name,
                                plant.createDate
                        )
                )
                .from(plant)
                .join(plant.place, place)
                .leftJoin(plant.waterings, watering)
                .where(plant.gardener.gardenerId.eq(gardenerId)
                        .and(watering.plant.plantId.isNull()))
                .fetch();
    }

    @Override
    public List<Plant> findAllPlants(Long gardenerId) {
        return queryFactory.selectFrom(plant)
                .join(plant.place, place)
                .where(plant.gardener.gardenerId.eq(gardenerId))
                .orderBy(plant.createDate.desc())
                .fetch();
    }

    @Override
    public Long countWateringByPlantId(Long plantId) {
        return queryFactory
                .select(watering.count())
                .from(watering)
                .where(watering.plant.plantId.eq(plantId))
                .fetchFirst();
    }

    @Override
    public LocalDate findLatestWateringDateByPlantId(Long plantId) {
        return queryFactory
                .select(watering.wateringDate.max())
                .from(watering)
                .where(watering.plant.plantId.eq(plantId))
                .fetchOne();
    }
}

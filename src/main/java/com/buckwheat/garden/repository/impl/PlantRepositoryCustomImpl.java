package com.buckwheat.garden.repository.impl;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.PlantRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.buckwheat.garden.data.entity.QPlace.place;
import static com.buckwheat.garden.data.entity.QPlant.plant;
import static com.buckwheat.garden.data.entity.QWatering.watering;

@RequiredArgsConstructor
public class PlantRepositoryCustomImpl implements PlantRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Plant> findWaitingForWateringList(Long gardenerId) {
        return queryFactory
                .selectFrom(plant)
                .join(plant.place, place)
                .leftJoin(plant.waterings, watering)
                .where(plant.gardener.gardenerId.eq(gardenerId)
                        .and(watering.plant.plantId.isNull()))
                .fetch();
    }
}
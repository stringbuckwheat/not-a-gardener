package xyz.notagardener.plant.plant.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.dto.QPlantResponse;
import xyz.notagardener.plant.garden.dto.QWaitingForWatering;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;

import java.util.List;
import java.util.Optional;

import static xyz.notagardener.place.QPlace.place;
import static xyz.notagardener.plant.QPlant.plant;
import static xyz.notagardener.status.model.QStatus.status;
import static xyz.notagardener.watering.QWatering.watering;

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
    public List<PlantResponse> findAllPlantsWithLatestWateringDate(Long gardenerId) {
        return queryFactory
                .select(
                        new QPlantResponse(
                                plant,
                                place,
                                watering.wateringId,
                                watering.wateringDate.max().as("latestWateringDate"),
                                watering.wateringDate.count().as("totalWatering").longValue(),
                                status
                        )
                )
                .from(plant)
                .join(plant.place, place)
                .leftJoin(plant.waterings, watering)
                .leftJoin(plant.status, status)
                .where(plant.gardener.gardenerId.eq(gardenerId))
                .groupBy(plant.plantId)
                .orderBy(plant.plantId.desc())
                .fetch();
    }

    public Optional<PlantResponse> findPlantWithLatestWateringDate(Long plantId, Long gardenerId) {
        PlantResponse plantResponse = queryFactory.select(
                        new QPlantResponse(
                                plant,
                                place,
                                watering.wateringId,
                                watering.wateringDate.max().as("latestWateringDate"),
                                watering.wateringDate.count().as("totalWatering").longValue(),
                                status
                        )
                )
                .from(plant)
                .join(plant.place, place)
                .leftJoin(plant.waterings, watering)
                .leftJoin(plant.status, status)
                .where(plant.plantId.eq(plantId)
                        .and(plant.gardener.gardenerId.eq(gardenerId)))
                .fetchOne();

        return Optional.ofNullable(plantResponse);
    }
}

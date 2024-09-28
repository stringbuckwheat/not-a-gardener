package xyz.notagardener.plant.plant.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.plant.garden.dto.QPlantResponse;
import xyz.notagardener.plant.garden.dto.QWaitingForWatering;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.plant.plant.dto.PlantBasic;
import xyz.notagardener.plant.plant.dto.QPlantBasic;

import java.util.List;
import java.util.Optional;

import static xyz.notagardener.place.model.QPlace.place;
import static xyz.notagardener.plant.model.QPlant.plant;
import static xyz.notagardener.status.common.model.QStatus.status;
import static xyz.notagardener.watering.model.QWatering.watering;

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
                .where(plant.plantId.eq(plantId))
                .fetchOne();

        return Optional.ofNullable(plantResponse);
    }

    @Override
    public List<PlantBasic> findAttentionNotRequiredPlants(Long gardenerId) {
        return queryFactory.select(
                        new QPlantBasic(plant)
                )
                .from(plant)
                .leftJoin(plant.status, status)
                .where(
                        (plant.status.attention.eq(YesOrNoType.N)
                                .or(plant.status.isNull()))
                                .and(plant.gardener.gardenerId.eq(gardenerId))
                )
                .fetch();
    }

    @Override
    public List<Plant> findAttentionRequiredPlants(Long gardenerId) {
        return queryFactory
                .selectFrom(plant)
                .join(plant.status, status)
                .where(
                        status.attention.eq(YesOrNoType.Y)
                                .and(plant.gardener.gardenerId.eq(gardenerId))
                )
                .fetch();
    }
}

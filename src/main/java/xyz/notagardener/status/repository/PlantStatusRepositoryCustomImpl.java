package xyz.notagardener.status.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.status.dto.QSimplePlantStatus;
import xyz.notagardener.status.dto.SimplePlantStatus;

import java.util.List;

import static xyz.notagardener.status.QPlantStatus.plantStatus;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantStatusRepositoryCustomImpl implements PlantStatusRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SimplePlantStatus> findByPlantId(Long plantId, Long gardenerId, List<String> statusList) {
        return queryFactory
                .select(
                        new QSimplePlantStatus(
                                plantStatus.status,
                                plantStatus.recordedDate.max()
                        )
                )
                .from(plantStatus)
                .where(
                        plantStatus.plant.plantId.eq(plantId)
                                .and(plantStatus.plant.gardener.gardenerId.eq(gardenerId))
                                .and(plantStatus.status.in(statusList))
                )
                .groupBy(plantStatus.status)
                .fetch();
    }
}

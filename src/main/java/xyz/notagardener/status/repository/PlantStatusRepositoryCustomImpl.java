package xyz.notagardener.status.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.PlantStatusType;
import xyz.notagardener.status.dto.QPlantStatusResponse;

import java.util.List;
import java.util.Optional;

import static xyz.notagardener.status.QPlantStatus.plantStatus;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantStatusRepositoryCustomImpl implements PlantStatusRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PlantStatusResponse> findByPlantIdAndStatus(Long plantId, PlantStatusType status) {
        List<PlantStatusResponse> result = queryFactory.select(
                        new QPlantStatusResponse(
                                plantStatus.plantStatusId,
                                plantStatus.status,
                                plantStatus.active,
                                plantStatus.recordedDate,
                                plantStatus.createDate
                        )
                )
                .from(plantStatus)
                .where(
                        plantStatus.plant.plantId.eq(plantId)
                                .and(plantStatus.status.eq(status))
                )
                .orderBy(plantStatus.recordedDate.desc(), plantStatus.createDate.desc())
                .limit(1)
                .fetch();

        return Optional.ofNullable(result.get(0));
    }

    @Transactional
    public Long deactivateStatusByPlantIdAndStatus(Long plantId, PlantStatusType status) {
        return queryFactory
                .update(plantStatus)
                .set(plantStatus.active, YesOrNoType.N)
                .where(plantStatus.plant.plantId.eq(plantId)
                        .and(plantStatus.status.eq(status)))
                .execute();
    }
}

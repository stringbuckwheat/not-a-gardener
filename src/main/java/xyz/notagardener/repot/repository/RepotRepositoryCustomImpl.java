package xyz.notagardener.repot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.plant.dto.QRepotList;
import xyz.notagardener.plant.plant.dto.RepotList;

import java.util.List;

import static xyz.notagardener.plant.QPlant.plant;
import static xyz.notagardener.repot.QRepot.repot;
import static xyz.notagardener.status.QPlantStatus.plantStatus;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepotRepositoryCustomImpl implements RepotRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<RepotList> findAll(Long gardenerId, Pageable pageable) {
        return queryFactory.select(
                        new QRepotList(
                                repot.repotId,
                                repot.repotDate,
                                repot.initPeriod,

                                plant.plantId,
                                plant.name,

                                plantStatus.plantStatusId,
                                plantStatus.status
                        )
                )
                .from(repot)
                .leftJoin(repot.plant, plant)
                .leftJoin(repot.plant.status, plantStatus)
                .where(repot.plant.gardener.gardenerId.eq(gardenerId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(repot.repotDate.desc())
                .fetch();
    }
}

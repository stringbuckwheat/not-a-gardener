package xyz.notagardener.repot.repot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.repot.plant.dto.QRepotList;
import xyz.notagardener.repot.plant.dto.RepotList;

import java.util.List;

import static xyz.notagardener.plant.QPlant.plant;
import static xyz.notagardener.repot.QRepot.repot;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepotRepositoryCustomImpl implements RepotRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<RepotList> findAll(Long gardenerId, Pageable pageable) {
        return queryFactory.select(
                        new QRepotList(
                                repot.repotId,
                                repot.repotDate,
                                repot.initPeriod,

                                plant.plantId,
                                plant.name
                        )
                )
                .from(repot)
                .join(repot.plant, plant)
                .where(repot.plant.gardener.gardenerId.eq(gardenerId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(repot.repotDate.desc())
                .fetch();
    }

    @Override
    public List<RepotList> findAllByPlantId(Long plantId, Pageable pageable) {
        return queryFactory.select(
                        new QRepotList(
                                repot.repotId,
                                repot.repotDate,
                                repot.initPeriod,

                                plant.plantId,
                                plant.name
                        )
                )
                .from(repot)
                .join(repot.plant, plant)
                .where(repot.plant.plantId.eq(plantId))
                .offset(pageable.getOffset()) // 시작지점
                .limit(pageable.getPageSize()) // 페이지 사이즈
                .orderBy(repot.repotDate.desc())
                .fetch();
    }
}

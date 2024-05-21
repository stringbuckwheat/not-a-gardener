package xyz.notagardener.gardener.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.gardener.QGardener;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.QGardenerDetail;

import java.util.Optional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GardenerRepositoryCustomImpl implements GardenerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GardenerDetail> findGardenerDetailByGardenerId(Long gardenerId) {
        GardenerDetail result = queryFactory.select(
                        new QGardenerDetail(
                                QGardener.gardener.gardenerId,
                                QGardener.gardener.username,
                                QGardener.gardener.email,
                                QGardener.gardener.name,
                                QGardener.gardener.createDate,
                                QGardener.gardener.provider
                        )
                )
                .from(QGardener.gardener)
                .where(QGardener.gardener.gardenerId.eq(gardenerId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}

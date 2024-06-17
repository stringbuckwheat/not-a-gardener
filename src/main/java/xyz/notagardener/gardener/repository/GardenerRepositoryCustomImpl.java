package xyz.notagardener.gardener.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.QGardenerDetail;
import xyz.notagardener.gardener.dto.QUsername;
import xyz.notagardener.gardener.dto.Username;

import java.util.List;
import java.util.Optional;

import static xyz.notagardener.gardener.model.QGardener.gardener;


@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GardenerRepositoryCustomImpl implements GardenerRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GardenerDetail> findGardenerDetailByGardenerId(Long gardenerId) {
        GardenerDetail result = queryFactory.select(
                        new QGardenerDetail(
                                gardener.gardenerId,
                                gardener.username,
                                gardener.email,
                                gardener.name,
                                gardener.createDate,
                                gardener.provider
                        )
                )
                .from(gardener)
                .where(gardener.gardenerId.eq(gardenerId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Username> findByProviderIsNullAndEmail(String email) {
        return queryFactory
                .select(new QUsername(gardener.username))
                .from(gardener)
                .where(gardener.provider.isNull().and(gardener.email.eq(email)))
                .fetch();
    }
}

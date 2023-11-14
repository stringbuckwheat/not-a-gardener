package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.projection.Username;
import com.buckwheat.garden.repository.query.querydsl.GardenerRepositoryCustom;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface GardenerQueryRepository extends Repository<Gardener, Long>, GardenerRepositoryCustom {
    Optional<Gardener> findById(Long gardenerId);
    Optional<Gardener> findByProviderIsNullAndUsername(String username);
    List<Username> findByProviderIsNullAndEmail(String email);

}

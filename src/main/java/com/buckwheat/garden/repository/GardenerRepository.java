package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Gardener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GardenerRepository extends JpaRepository<Gardener, Long> {
    Optional<Gardener> findByUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);
    List<Gardener> findByEmailAndProviderIsNull(String email);
}

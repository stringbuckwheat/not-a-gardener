package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.projection.Username;
import com.buckwheat.garden.data.entity.Gardener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GardenerRepository extends JpaRepository<Gardener, Long> {
    Optional<Gardener> findByUsername(String username);
    Optional<Gardener> findByUsernameAndProvider(String username, String provider);

    @Query(value = "SELECT gardener.username as username" +
            " FROM Gardener gardener" +
    " WHERE gardener.email = :email" +
    " AND gardener.provider IS NULL")
    List<Username> findByEmailAndProviderIsNull(@Param("email") String email);
}

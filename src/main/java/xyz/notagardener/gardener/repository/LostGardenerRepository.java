package xyz.notagardener.gardener.repository;

import org.springframework.data.repository.Repository;
import xyz.notagardener.gardener.dto.LostGardener;

import java.util.Optional;

public interface LostGardenerRepository extends Repository<LostGardener, String> {
    LostGardener save(LostGardener lostGardener);
    Optional<LostGardener> findById(String identificationCode);
    void deleteById(String identificationCode);
}

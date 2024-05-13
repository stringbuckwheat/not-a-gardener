package xyz.notagardener.gardener.forgot;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface LostGardenerRepository extends Repository<LostGardener, String> {
    LostGardener save(LostGardener lostGardener);
    Optional<LostGardener> findById(String identificationCode);
    void deleteById(String identificationCode);
}

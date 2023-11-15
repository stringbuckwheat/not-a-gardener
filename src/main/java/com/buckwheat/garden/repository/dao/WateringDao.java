package com.buckwheat.garden.repository.dao;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.query.querydsl.WateringRepositoryCustom;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface WateringDao extends Repository<Watering, Long>, WateringRepositoryCustom {
    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);

    Optional<Watering> findById(Long wateringId);

    Watering save(Watering watering);

    void deleteById(Long wateringId);
}
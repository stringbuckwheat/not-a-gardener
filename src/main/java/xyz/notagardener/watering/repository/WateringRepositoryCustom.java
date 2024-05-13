package xyz.notagardener.domain.watering.repository;

import xyz.notagardener.domain.watering.Watering;
import xyz.notagardener.domain.watering.dto.ChemicalUsage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WateringRepositoryCustom {
    List<Watering> findAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);

    LocalDate findLatestWateringDate(Long plantId);

    List<ChemicalUsage> findLatestChemicalizedDayList(Long gardenerId, Long plantId, String active);

    Boolean existByWateringDateAndPlantId(LocalDate wateringDate, Long plantId);

    List<Watering> findLatestFourWateringDate(Long plantId);

    List<Watering> findWateringsByPlantIdWithPage(Long plantId, Pageable pageable);
}

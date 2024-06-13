package xyz.notagardener.repot.repot.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.repot.plant.dto.RepotList;

import java.util.List;

public interface RepotRepositoryCustom {
    List<RepotList> findAll(Long gardenerId, Pageable pageable);
    List<RepotList> findAllByPlantId(Long plantId, Pageable pageable);
}

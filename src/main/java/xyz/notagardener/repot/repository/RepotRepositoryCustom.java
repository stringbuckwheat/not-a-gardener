package xyz.notagardener.repot.repository;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.plant.plant.dto.RepotList;

import java.util.List;

public interface RepotRepositoryCustom {
    List<RepotList> findAll(Long gardenerId, Pageable pageable);
}

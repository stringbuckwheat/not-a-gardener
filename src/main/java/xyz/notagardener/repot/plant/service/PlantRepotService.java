package xyz.notagardener.repot.plant.service;

import org.springframework.data.domain.Pageable;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.repot.dto.RepotRequest;

import java.util.List;

public interface PlantRepotService {
    List<RepotList> getAllRepotForOnePlant(Long plantId, Pageable pageable);
    RepotList add(RepotRequest repotRequest, Long gardenerId);
}

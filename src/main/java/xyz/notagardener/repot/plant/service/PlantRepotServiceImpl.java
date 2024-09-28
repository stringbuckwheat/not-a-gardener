package xyz.notagardener.repot.plant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.notagardener.repot.model.Repot;
import xyz.notagardener.repot.plant.dto.RepotList;
import xyz.notagardener.repot.repot.dto.RepotRequest;
import xyz.notagardener.repot.repot.repository.RepotRepository;
import xyz.notagardener.repot.repot.service.RepotCommandService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantRepotServiceImpl implements PlantRepotService {
    private final RepotCommandService repotCommandService;
    private final RepotRepository repotRepository;

    @Override
    public List<RepotList> getAllRepotForOnePlant(Long plantId, Pageable pageable) {
        return repotRepository.findAllByPlantId(plantId, pageable);
    }

    @Override
    public RepotList add(RepotRequest repotRequest, Long gardenerId) {
        Repot repot = repotCommandService.addOne(repotRequest, gardenerId);
        return new RepotList(repot);
    }
}

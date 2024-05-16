package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.watering.dto.ChemicalUsage;
import xyz.notagardener.watering.repository.WateringRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChemicalInfoServiceImpl implements ChemicalInfoService {
    private final WateringRepository wateringRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChemicalUsage> getChemicalUsagesByPlantId(Long plantId, Long gardenerId) {
        return wateringRepository.findLatestChemicalizedDayList(gardenerId, plantId, "Y");
    }
}

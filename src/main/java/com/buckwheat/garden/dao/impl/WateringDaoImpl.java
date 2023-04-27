package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.ChemicalUsage;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class WateringDaoImpl implements WateringDao {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    @Override
    public Watering addWatering(WateringDto.Request wateringRequest){
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);

        // 저장
        return wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
    }

    @Override
    public List<Watering> getWateringListByPlantNo(Long plantId) {
        return wateringRepository.findByPlant_PlantIdOrderByWateringDateDesc(plantId);
    }

    @Override
    public List<Watering> getAllWateringListByMemberNo(Long memberId, LocalDate startDate, LocalDate endDate){
        return wateringRepository.findAllWateringListByMemberNo(memberId, startDate, endDate);
    }


    @Override
    public List<ChemicalUsage> getLatestChemicalUsages(Long memberId, Long plantId) {
        return wateringRepository.findLatestChemicalizedDayList(memberId, plantId);
    }

    @Override
    public Watering modifyWatering(WateringDto.Request wateringRequest){
        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantRepository.findByPlantId(wateringRequest.getPlantId())
                .orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalId()).orElse(null);

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getId())
                .orElseThrow(NoSuchElementException::new);

        // 수정
        wateringRepository.save(watering.update(wateringRequest.getDate(), plant, chemical));
        return watering.update(wateringRequest.getDate(), plant, chemical);
    }

    @Override
    public void deleteById(Long id){
        wateringRepository.deleteById(id);
    }

    @Override
    public void deleteByPlantId(Long plantId){
        wateringRepository.deleteAllByPlant_PlantId(plantId);
    }
}

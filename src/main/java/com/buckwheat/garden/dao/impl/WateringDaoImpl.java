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
        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);
        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElse(null);

        // 저장
        return wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
    }

    @Override
    public List<Watering> getWateringListByPlantNo(int plantNo){
        return wateringRepository.findByPlant_plantNoOrderByWateringDateDesc(plantNo);
    }

    @Override
    public List<Watering> getAllWateringListByMemberNo(int memberNo, LocalDate startDate, LocalDate endDate){
        return wateringRepository.findAllWateringListByMemberNo(memberNo, startDate, endDate);
    }

    @Override
    public List<ChemicalUsage> getLatestChemicalUsages(int plantNo, int memberNo) {
        return wateringRepository.findLatestChemicalizedDayList(plantNo, memberNo);
    }

    @Override
    public Watering modifyWatering(WateringDto.Request wateringRequest){
        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElse(null);

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getWateringNo())
                .orElseThrow(NoSuchElementException::new);

        // 수정
        wateringRepository.save(watering.update(wateringRequest.getWateringDate(), plant, chemical));
        return watering.update(wateringRequest.getWateringDate(), plant, chemical);
    }

    @Override
    public void removeWatering(int wateringNo){
        wateringRepository.deleteById(wateringNo);
    }

    @Override
    public void removeAllWateringByPlantNo(int plantNo){
        wateringRepository.deleteAllByPlant_plantNo(plantNo);
    }
}

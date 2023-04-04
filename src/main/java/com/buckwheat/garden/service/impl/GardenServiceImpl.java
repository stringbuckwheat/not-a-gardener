package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.GardenService;
import com.buckwheat.garden.util.GardenUtil;
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantRepository plantRepository;
    private final ChemicalRepository chemicalRepository;
    private final WateringRepository wateringRepository;
    private final GardenUtil gardenUtil;
    private final WateringUtil wateringUtil;

    @Override
    public GardenDto.GardenMain getGarden(int memberNo) {
        List<GardenDto.GardenResponse> todoList = new ArrayList<>();
        List<GardenDto.WaitingForWatering> waitingList = new ArrayList<>(); // plantNo, plantName;

        List<Chemical> chemicalList = chemicalRepository.findByMember_memberNoOrderByChemicalPeriodDesc(memberNo);
        List<Plant> plantList = plantRepository.findByMember_MemberNo(memberNo);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {
            int wateringDDay = gardenUtil.getWateringDDay(plant.getAverageWateringPeriod(), gardenUtil.getLastDrinkingDay(plant));
            int wateringCode = gardenUtil.getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);

            boolean hasToDo = wateringCode < 0 || wateringCode == 1 || wateringCode == 2;

            if(wateringCode == 0){
                waitingList.add(new GardenDto.WaitingForWatering(plant.getPlantNo(), plant.getPlantName()));
                continue;
            }

            if(hasToDo){
                PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
                GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalList, wateringDDay, wateringCode);

                todoList.add(new GardenDto.GardenResponse(plantResponse, gardenDetail));
            }
        }

        return new GardenDto.GardenMain(todoList, waitingList);
    }

    @Override
    public GardenDto.GardenResponse getGardenResponse(Plant plant, List<Chemical> chemicalList) {
        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalList);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    public GardenDto.GardenResponse getGardenResponse(int plantNo, int memberNo){
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);
        List<Chemical> chemicalList = chemicalRepository.findByMember_memberNoOrderByChemicalPeriodDesc(memberNo);

        PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
        GardenDto.GardenDetail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalList);

        return new GardenDto.GardenResponse(plantResponse, gardenDetail);
    }

    @Override
    public GardenDto.WateringResponse addWateringInGarden(Member member, WateringDto.WateringRequest wateringRequest) {
        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);
        log.debug("plant: {}", PlantDto.PlantResponse.from(plant));

        // 물주기 저장 후 메시지 받아옴
        WateringDto.WateringMsg wateringMsg = wateringUtil.addWatering(plant, wateringRequest);

        // 필요시 물주기 정보 업데이트
        wateringUtil.getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        // watering 저장한 게 반영이 안 되어 있음
        GardenDto.GardenResponse gardenResponse = getGardenResponse(plant.getPlantNo(), member.getMemberNo());

//        Map<String, Object> map = new HashMap<>();
//        map.put("wateringMsg", wateringMsg);
//        map.put("gardenResponse", gardenResponse);

        return new GardenDto.WateringResponse(gardenResponse, wateringMsg);
    }
}

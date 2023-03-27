package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.ChemicalDto;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.GardenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final PlantRepository plantRepository;
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;

    @Override
    public List<GardenDto.GardenResponse> getGarden(int memberNo) {
        List<GardenDto.GardenResponse> gardenList = new ArrayList<>();

        List<Chemical> chemicalList = chemicalRepository.findByMember_memberNoOrderByChemicalPeriodDesc(memberNo);
        List<Plant> plantList = plantRepository.findByMember_MemberNo(memberNo);

        // 필요한 것들 계산해서 gardenDto list 리턴
        for (Plant plant : plantList) {
            List<Watering> wateringList = plant.getWateringList();
            String anniversary = "";

            // 키운 지 며칠이나 지났는지 (nullable)
            if (plant.getBirthday() != null) {
                anniversary = getAnniversary(plant.getBirthday());
//                log.debug("{}: {}", plant.getPlantName(), anniversary);
            }

            WateringDto.WateringResponse latestWatering = null;
            if(plant.getWateringList().size() > 0) {
                latestWatering = WateringDto.WateringResponse.from(plant.getWateringList().get(0));
            }

            // watering code: 계산 값에 따라 화면에서 물주기, 물줄 날짜 놓침 등의 메시지를 띄울 용도
            // 물을 준 적이 한 번도 없는 경우
            int wateringDDay = getWateringDDay(plant.getAverageWateringPeriod(), getLastDrinkingDay(plant));
//            log.debug("{}의 wateringDDay: {}일 남음", plant.getPlantName(), wateringDDay);

            int wateringCode = getWateringCode(plant.getAverageWateringPeriod(), wateringDDay);
//            log.debug("{}의 watering code: {}", plant.getPlantName(), wateringCode);

            // chemicalCode: 물을 줄 식물에 대해서 맹물을 줄지 비료/약품 희석액을 줄지 알려주는 용도
            int chemicalCode = getChemicalCode(plant.getPlantNo(), chemicalList); // FertilizerNo or -1

            PlantDto.PlantResponse plantResponse = PlantDto.PlantResponse.from(plant);
            GardenDto.GardenDetail gardenDetail = GardenDto.GardenDetail.from(latestWatering, anniversary, wateringDDay, wateringCode, chemicalCode);

            gardenList.add(new GardenDto.GardenResponse(plantResponse, gardenDetail));
        }

        log.debug("gardenList: {}", gardenList);
        return gardenList;
    }

    private String getAnniversary(LocalDate birthday) {
        LocalDate today = LocalDate.now();
        // 생일이면
        if ((today.getMonth() == birthday.getMonth()) && (today.getDayOfMonth() == birthday.getDayOfMonth())) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
    }

    private LocalDate getLastDrinkingDay(Plant plant) {
        // 물주기 정보가 있으면 진짜 제일 최근 물 준 날짜를 리턴
        if (plant.getWateringList().size() != 0) {
            return plant.getWateringList().get(0).getWateringDate();
        }

        return plant.getCreateDate().toLocalDate();
    }


    private int getWateringDDay(int recentWateringPeriod, LocalDate lastDrinkingDay) {
        // 비료든 물이든 뭐라도 준지 며칠이나 지났는지 계산
        int period = (int) Duration.between(lastDrinkingDay.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
        return recentWateringPeriod - period;
    }

    private int getWateringCode(int recentWateringPeriod, int wateringDDay) {
        // 음수           0             1          2         3         4
        // 물주기 놓침   물주기 정보 부족    물주기     체크하기     놔두세요    오늘 물 줌

        int wateringCode = 999;

        if (recentWateringPeriod == 0) {
            // 물주기 정보 부족
            wateringCode = 0;
        } else if (wateringDDay == 0) {
            // 물주기
            wateringCode = 1;
        } else if (wateringDDay == 1) {
            // 물주기 하루 전
            // 체크하세요
            wateringCode = 2;
        } else if(recentWateringPeriod == wateringDDay){
            // 오늘 물 줌
            wateringCode = 4;
        } else if (wateringDDay >= 2) { // 얘가 wateringCode == 4 보다 먼저 걸린다
            // 물주기까지 이틀 이상 남음
            // 놔두세요
            wateringCode = 3;
        } else if (wateringDDay < 0) {

            // 음수가 나왔으면 물주기 놓침
            // 며칠 늦었는지 알려줌
            wateringCode = wateringDDay;
        }

        return wateringCode;
    }

    // -1           0           1
    // 비료 사용 안함  맹물 주기
    public int getChemicalCode(int plantNo, List<Chemical> chemicalList) {
        // chemicalList는 시비 주기가 느린 순으로 들어있음
        for(Chemical chemical : chemicalList){
            log.debug(ChemicalDto.ChemicalResponse.from(chemical).toString());
        }



        // 가진 chemical 목록과 같은 크기의 arraylist 생성
        List<LocalDate> latestFertilizedDates = new ArrayList<>(chemicalList.size());
        List<Map<String, Object>> fertilizingCodes = new ArrayList<>(chemicalList.size());

        // chemical list Example
        // 1011     1015    1016    4055    5011
        // 하이포     개화용   미량원소   코니도    살균제
        // 14일      14일     60일    14일      50일

        // chemicalList 인덱스에 맞춰 latestFertilizedDates 리스트 만들기
        // 해당 식물의 가장 최근 해당 chemical을 관주한 날을 기록한다
        for(Chemical chemical : chemicalList){
            LocalDate fertilizingDate = wateringRepository.findLatestFertilizingDayByPlantNoAndChemicalNo(plantNo, chemical.getChemicalNo());
            latestFertilizedDates.add(fertilizingDate);
        }

        // index 필요
        // chemical list index에 맞춰 해당 chemical을 줘야하는지 말아야하는지 산출
        for(int i = 0; i < latestFertilizedDates.size(); i++){
            LocalDate date = latestFertilizedDates.get(i);

            if(latestFertilizedDates.get(i) == null){
                // 해당 비료를 준 기록이 아예 없으면
                // TODO 제일 첫 비료 주기 알림은 어떻게 할지 고민해볼 것
                continue;
            }

            // 해당 비료를 준지 얼마나 지났는지 계산
            int period = (int) Duration.between(date.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

            // 시비 날짜와 같거나 더 지났으면
            if(period >= chemicalList.get(i).getChemicalPeriod()){
                return chemicalList.get(i).getChemicalNo();
            }
        }

        return -1;
    }

}

package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Fertilizer;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.FertilizerRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class WateringServiceImpl implements WateringService {
    private final WateringRepository wateringRepository;
    private final FertilizerRepository fertilizerRepository;
    private final PlantRepository plantRepository;

    @Override
    public List<WateringDto.WateringList> getWateringList(int memberNo) {
        // return용 리스트
        List<WateringDto.WateringList> wateringDtoList = new ArrayList<>();

        // member로 plantList 가져와서
        // @EntityGraph 메소드
        List<Plant> plantList = plantRepository.findByMember_MemberNoOrderByCreateDateDesc(memberNo);

        // plant 별 watering list를 가져온 뒤
        for(Plant plant : plantList){
            List<Watering> wateringList = plant.getWateringList();

            for(Watering watering : wateringList){
                wateringDtoList.add(
                    WateringDto.WateringList.from(watering)
                );
            }
        }

        // TODO 정렬

        return wateringDtoList;
    }

    @Override
    public WateringDto.WateringResponse addWatering(WateringDto.WateringRequest wateringRequest) {
        Plant plant = plantRepository.findById(wateringRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);

        // 맹물을 줬으면
        if(wateringRequest.getFertilizerNo() == 0){
            Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlant(plant));
            return WateringDto.WateringResponse.from(watering);
        }

        // 비료도 줬으면
        Fertilizer fertilizer = fertilizerRepository.findById(wateringRequest.getFertilizerNo()).orElseThrow(NoSuchElementException::new);
        Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndFertilizer(plant, fertilizer));
        return WateringDto.WateringResponse.from(watering);
    }

    /**
     *
     * @param list 물 준 날짜의 DESC
     * @return
     */
    public List<WateringDto.WateringForOnePlant> withWateringPeriodList(List<Watering> list){
        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            if(i == list.size() - 1){
                wateringList.add(
                        WateringDto.WateringForOnePlant.from(list.get(i))
                );

                continue;
            }

            // 두 번째 데이터 부터는
            LocalDate afterWateringDate = list.get(i).getWateringDate();
            LocalDate prevWateringDate = list.get(i+1).getWateringDate();

            int wateringPeriod = Period.between(prevWateringDate, afterWateringDate).getDays();

            wateringList.add(
                    WateringDto.WateringForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod)
            );
        }

        log.debug("wateringList: {}", wateringList);

        return wateringList;
    }

    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo) {
        List<Watering> list = wateringRepository.findByPlant_plantNoOrderByWateringDateDesc(plantNo);

        // 며칠만에 물 줬는지도 계산해줌
        if(list.size() >= 2){
            return withWateringPeriodList(list);
        }

        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for(Watering watering : list){
            wateringList.add(
                    WateringDto.WateringForOnePlant.from(watering)
            );
        }

        return wateringList;
    }

    // 지금은 save 로직과 같지만 일단은 분리
    @Override
    public WateringDto.WateringResponse modifyWatering(WateringDto.WateringRequest wateringRequest) {
        Plant plant = plantRepository.findById(wateringRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Fertilizer fertilizer = fertilizerRepository.findById(wateringRequest.getFertilizerNo()).orElseThrow(NoSuchElementException::new);

        Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndFertilizer(plant, fertilizer));

        return WateringDto.WateringResponse.from(watering);
    }

    @Override
    public void deleteWatering(int wateringNo) {
        wateringRepository.deleteById(wateringNo);
    }

    @Override
    public int getWateringPeriodCode(int plantNo){
        // 이전 물주기와 비교
        int prevWateringPeriod = plantRepository.findById(plantNo)
                .orElseThrow(NoSuchElementException::new)
                .getAverageWateringPeriod();

        // DB에서 마지막 물주기 row 2개 들고와서 주기 계산
        List<Watering> recentWateringList = wateringRepository.findTop2ByPlant_PlantNoOrderByWateringNoDesc(plantNo);
        log.debug("recentWateringList: " + recentWateringList);

        if(recentWateringList.size() == 1){
            // 물주기 기록이 오직 한 개인 경우
            // TODO plant 테이블의 create_date과 비교하거나, 데이터 두 개 모일 때까지 기다리거나!
            return 0;
        }

        int tmpPeriod = Period.between(
                recentWateringList.get(1).getWateringDate(),
                recentWateringList.get(0).getWateringDate())
                .getDays();

        if(prevWateringPeriod > tmpPeriod){
            // 물주기가 짧아진 경우 -> DB에 반영
            Plant plant = plantRepository.findById(plantNo).orElseThrow(NoSuchElementException::new);
            plant.setAverageWateringPeriod(tmpPeriod);

            // plantNo 값이 있으므로 update가 실행된다.
            plantRepository.save(plant);

            return -1;
        } else if (prevWateringPeriod == tmpPeriod){
            // 물주기 똑같음
            return 0;
        } else {
            // 물주기 길어짐
            // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
            return 1;
        }
    }
}

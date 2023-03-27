package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
import com.buckwheat.garden.service.WateringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class WateringServiceImpl implements WateringService {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;

    /**
     * 이번 관수가 며칠만인지 계산
     *
     * @param latestWateringDate DB에 저장된 가장 최근 물주기
     * @return 이번 관수가 며칠만인지
     */
    public int calculateWateringPeriod(LocalDateTime latestWateringDate) {
        // 최근 물주기가 변경되었는지 확인하기
        // ex. 그간 나흘마다 물을 주다가 사흘만에 흙이 마르게 되었다.

        // 이번 관수가 며칠만인지
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return (int) Duration.between(latestWateringDate, today).toDays();
    }

    @Override
    public Plant getPlantForAdd(Plant plant, int period) {
        if (period != plant.getAverageWateringPeriod()) {
            Plant updatedPlant = plant.updateAverageWateringPeriod(period);
            plantRepository.save(updatedPlant);
            return updatedPlant;
        }

        return plant;
    }

    @Override
    public int getWateringCode(int period, int prevWateringPeriod) {
        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
        return Integer.compare(period, prevWateringPeriod);
    }

    /**
     * 물주기 기록 추가하기(무조건 '오늘' 버전 메소드)
     *
     * @param wateringRequest
     * @return WateringResponseDto
     */
    @Override
    public WateringDto.WateringModifyResponse addWatering(WateringDto.WateringRequest wateringRequest) {
        log.debug("add watering");
        log.debug("wateringRequest: {}", wateringRequest);

        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElse(null);

        // 물주기 정보가 없으면
        // 즉, 첫번째 물주기면
        if (plant.getWateringList().size() == 0) {
            Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));
            WateringDto.WateringMsg wateringMsg = new WateringDto.WateringMsg(2, plant.getAverageWateringPeriod());

            List<WateringDto.WateringForOnePlant> list = new ArrayList<>();
            list.add(WateringDto.WateringForOnePlant.from(watering));

            return WateringDto.WateringModifyResponse.builder()
                    .wateringMsg(wateringMsg)
                    .wateringList(list)
                    .build();
        }

        // 저장
        Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));

        // 리턴용 DTO 만들기
        WateringDto.WateringMsg wateringMsg = getWateringMsg(plant.getPlantNo());
        log.debug("wateringMsg: {}", wateringMsg);
        List<WateringDto.WateringForOnePlant> wateringList = getWateringListForPlant(wateringRequest.getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if(wateringMsg.getWateringCode() == 3){
            return WateringDto.WateringModifyResponse.builder()
                    .wateringMsg(wateringMsg)
                    .wateringList(wateringList)
                    .build();
        }

        // 필요시 물주기 정보 업데이트
        Plant plantForAdd = getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        return WateringDto.WateringModifyResponse.builder()
                .plant(PlantDto.PlantResponse.from(plantForAdd))
                .wateringMsg(wateringMsg)
                .wateringList(wateringList)
                .build();
    }

    /**
     * @param list 물 준 날짜의 DESC
     * @return
     */
    public List<WateringDto.WateringForOnePlant> withWateringPeriodList(List<Watering> list) {
        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                wateringList.add(
                        WateringDto.WateringForOnePlant.from(list.get(i))
                );

                continue;
            }

            // 두 번째 데이터 부터는
            LocalDate afterWateringDate = list.get(i).getWateringDate();
            LocalDate prevWateringDate = list.get(i + 1).getWateringDate();

            int wateringPeriod = Period.between(prevWateringDate, afterWateringDate).getDays();

            wateringList.add(
                    WateringDto.WateringForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod)
            );
        }

        log.debug("wateringList: {}", wateringList);

        return wateringList;
    }

    @Override
    public List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo) {
        List<Watering> list = wateringRepository.findByPlant_plantNoOrderByWateringDateDesc(plantNo);

        // 며칠만에 물 줬는지도 계산해줌
        if (list.size() >= 2) {
            return withWateringPeriodList(list);
        }

        List<WateringDto.WateringForOnePlant> wateringList = new ArrayList<>();

        for (Watering watering : list) {
            wateringList.add(
                    WateringDto.WateringForOnePlant.from(watering)
            );
        }

        return wateringList;
    }

    public int calculateWateringPeriod(int plantNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        if (plant.getWateringList().size() > 1) {
            LocalDateTime latestWateringDate = plant.getWateringList().get(0).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = plant.getWateringList().get(1).getWateringDate().atStartOfDay();

            return (int) Duration.between(prevWateringDate, latestWateringDate).toDays();
        }

        return plant.getAverageWateringPeriod();
    }

    WateringDto.WateringMsg getWateringMsg(int plantNo) {
        log.debug("getWateringMsg()");
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);
        log.debug("plant: {}", PlantDto.PlantResponse.from(plant));

        List<WateringDto.WateringForOnePlant> list = new ArrayList();
        for(Watering w: plant.getWateringList()){
            list.add(WateringDto.WateringForOnePlant.from(w));
        }

        log.debug("watering list : {}", list);
        log.debug("plant.getWateringList().size(): {}", plant.getWateringList().size());

        // 이 메소드가 호출되는 시점엔 물주기 기록이 두 개 이상 있음
        LocalDateTime latestWateringDate = plant.getWateringList().get(0).getWateringDate().atStartOfDay();
        log.debug("latestWateringDate: {}", latestWateringDate);
        LocalDateTime prevWateringDate = plant.getWateringList().get(1).getWateringDate().atStartOfDay();
        log.debug("prevWateringDate: {}", prevWateringDate);

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();
        log.debug("period: {}", period);
        int wateringCode = getWateringCode(period, plant.getAverageWateringPeriod());
        log.debug("wateringCode: {}", wateringCode);

        return new WateringDto.WateringMsg(wateringCode, period);
    }

    @Override
    public WateringDto.WateringModifyResponse modifyWatering(WateringDto.WateringRequest wateringRequest) {
        log.debug("wateringRequest: {}", wateringRequest);

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

        // 리턴용 DTO 만들기
        WateringDto.WateringMsg wateringMsg = getWateringMsg(plant.getPlantNo());
        List<WateringDto.WateringForOnePlant> wateringList = getWateringListForPlant(wateringRequest.getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if(wateringMsg.getWateringCode() == 3){
            return WateringDto.WateringModifyResponse.builder()
                    .wateringMsg(wateringMsg)
                    .wateringList(wateringList)
                    .build();
        }

        // 최근 평균 물주기 update
        Plant plantForAdd = getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        log.debug("response: {}", WateringDto.WateringModifyResponse.builder()
                .plant(PlantDto.PlantResponse.from(plantForAdd))
                .wateringMsg(wateringMsg)
                .wateringList(wateringList)
                .build());

        return WateringDto.WateringModifyResponse.builder()
                .plant(PlantDto.PlantResponse.from(plantForAdd))
                .wateringMsg(wateringMsg)
                .wateringList(wateringList)
                .build();
    }

    @Override
    public void deleteWatering(int wateringNo) {
        wateringRepository.deleteById(wateringNo);
    }

    @Override
    public void deleteAllFromPlant(int plantNo) {
        wateringRepository.deleteAllByPlant_plantNo(plantNo);
    }
}

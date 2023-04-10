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
import com.buckwheat.garden.util.WateringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WateringServiceImpl implements WateringService {
    private final WateringRepository wateringRepository;
    private final ChemicalRepository chemicalRepository;
    private final PlantRepository plantRepository;
    private final WateringUtil wateringUtil;

    @Override
    public Map<LocalDate, List<WateringDto.ByDate>> getWateringList(int memberNo, int month) {
        // 1일이 일요일이면 뒤로 2주 더
        // 1일이 일요일이 아니면 앞으로 한 주 뒤로 한 주
        LocalDate firstDayOfMonth = LocalDate.of(2023, month, 1);

        // 월 화 수 목 금 토 일 => 1, 2, 3, 4, 5, 6, 7
        // 일요일이면 가만히 두고, 나머지 요일이면 getDayOfWeek().getValue()를 빼면
        // 7로 나눈 나머지를 뺌
        LocalDate startDate = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7);
        log.debug("startDate: {}", startDate.toString());

        // end date: 합쳐서 42가 되게 만드는 값
        // x = 42 - 이번달 - startDate으로 더한 값
        int tmp = 42 - firstDayOfMonth.lengthOfMonth() - firstDayOfMonth.getDayOfWeek().getValue() % 7;
        log.debug("tmp: {}", tmp);
        LocalDate endDate = firstDayOfMonth.plusDays(firstDayOfMonth.lengthOfMonth() - 1 + tmp);
        log.debug("endDate: {}", endDate.toString());

        Map<LocalDate, List<WateringDto.ByDate>> map = new HashMap<>(); // 날짜: 리스트

        for (Watering watering : wateringRepository.findAllWateringListByMemberNo(memberNo, startDate, endDate)) {
            List<WateringDto.ByDate> tmpList = map.get(watering.getWateringDate());

            if(tmpList == null){
                List<WateringDto.ByDate> list = new ArrayList<>();
                list.add(WateringDto.ByDate.from(watering));
                map.put(watering.getWateringDate(), list);
                continue;
            }

            tmpList.add(WateringDto.ByDate.from(watering));
            map.put(watering.getWateringDate(), tmpList);
        }

        log.debug("map: {}", map);
        return map;
    }

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

    /**
     * 물주기 기록 추가하기(무조건 '오늘' 버전 메소드)
     *
     * @param wateringRequest
     * @return WateringResponseDto
     */
    @Override
    public WateringDto.WateringModifyResponse addWatering(WateringDto.WateringRequest wateringRequest) {
        Plant plant = plantRepository.findByPlantNo(wateringRequest.getPlantNo())
                .orElseThrow(NoSuchElementException::new);

        WateringDto.WateringMsg wateringMsg = wateringUtil.addWatering(plant, wateringRequest);

        // 리턴용 DTO 만들기
        // WateringDto.WateringMsg wateringMsg = wateringUtil.getWateringMsg(plant.getPlantNo());
        log.debug("wateringMsg: {}", wateringMsg);
        List<WateringDto.WateringForOnePlant> wateringList = getWateringListForPlant(wateringRequest.getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if (wateringMsg.getAfterWateringCode() == 3) {
            return WateringDto.WateringModifyResponse.builder()
                    .wateringMsg(wateringMsg)
                    .wateringList(wateringList)
                    .build();
        }

        // 필요시 물주기 정보 업데이트
        Plant plantForAdd = wateringUtil.getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

        return WateringDto.WateringModifyResponse.builder()
                .plant(PlantDto.PlantResponse.from(plantForAdd))
                .wateringMsg(wateringMsg)
                .wateringList(wateringList)
                .build();
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

    public int calculateWateringPeriod(int plantNo) {
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);

        if (plant.getWateringList().size() > 1) {
            LocalDateTime latestWateringDate = plant.getWateringList().get(0).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = plant.getWateringList().get(1).getWateringDate().atStartOfDay();

            return (int) Duration.between(prevWateringDate, latestWateringDate).toDays();
        }

        return plant.getAverageWateringPeriod();
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
        WateringDto.WateringMsg wateringMsg = wateringUtil.getWateringMsg(plant.getPlantNo());
        List<WateringDto.WateringForOnePlant> wateringList = getWateringListForPlant(wateringRequest.getPlantNo());

        // 식물 테이블의 averageWateringDate 업데이트 필요 X
        if (wateringMsg.getAfterWateringCode() == 3) {
            return WateringDto.WateringModifyResponse.builder()
                    .wateringMsg(wateringMsg)
                    .wateringList(wateringList)
                    .build();
        }

        // 최근 평균 물주기 update
        Plant plantForAdd = wateringUtil.getPlantForAdd(plant, wateringMsg.getAverageWateringDate());

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

package com.buckwheat.garden.service.impl;

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

        return wateringDtoList;
    }

    /**
     * 이번 관수가 며칠만인지 계산
     * @param latestWateringDate DB에 저장된 가장 최근 물주기
     * @return 이번 관수가 며칠만인지
     */
    public int calculateWateringPeriod(LocalDateTime latestWateringDate){
        // 최근 물주기가 변경되었는지 확인하기
        // ex. 그간 나흘마다 물을 주다가 사흘만에 흙이 마르게 되었다.

        // 이번 관수가 며칠만인지
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return (int) Duration.between(latestWateringDate, today).toDays();
    }

    Plant getPlantForAdd(Plant plant, int period){
        log.debug("getPlantForAdd()");

        if(period != plant.getAverageWateringPeriod()){
            log.debug("average watering period 변경");
            log.debug("{}일 -> {}일", plant.getAverageWateringPeriod(), period);
            return plantRepository.save(plant.updateAverageWateringPeriod(period));
        }

        log.debug("average watering period 변경 안됨");

        return plant;
    }

    Watering getWateringEntity(WateringDto.WateringRequest wateringRequest, Plant plant){
        // 비료를 줬으면 Chemical를 매핑한 entity 리턴
        if(wateringRequest.getChemicalNo() != 0){
            Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElseThrow(NoSuchElementException::new);
            return wateringRequest.toEntityWithPlantAndChemical(plant, chemical);
        }

        // 맹물을 줬으면 plant만 매핑해서 리턴
        return wateringRequest.toEntityWithPlant(plant);
    }

    int getWateringCode(int period, int prevWateringPeriod){
        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
        return Integer.compare(period, prevWateringPeriod);
    }

    public WateringDto.WateringResponse addFirstWatering(Plant prevPlant, WateringDto.WateringRequest wateringRequest){
        Watering watering = wateringRepository.save(getWateringEntity(wateringRequest, prevPlant));
        WateringDto.WateringMsg wateringMsg = new WateringDto.WateringMsg(2, prevPlant.getAverageWateringPeriod());

        return WateringDto.WateringResponse.withWateringMsgFrom(watering, wateringMsg);
    }

    /**
     * 물주기 기록 추가하기(무조건 '오늘' 버전 메소드)
     * @param wateringRequest
     * @return WateringResponseDto
     */
    @Override
    public WateringDto.WateringResponse addWatering(WateringDto.WateringRequest wateringRequest) {
        // @EntityGraph(attributePaths = {"place", "wateringList"}, type= EntityGraph.EntityGraphType.FETCH)
        Plant prevPlant = plantRepository.findByPlantNo(wateringRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);

        // 물주기 정보가 없으면
        // 즉, 첫번째 물주기면
        if(prevPlant.getWateringList().size() == 0){
            return addFirstWatering(prevPlant, wateringRequest);
        }

        // 이번 물주기가 며칠만인지 계산
        int period = calculateWateringPeriod(prevPlant.getWateringList().get(0).getWateringDate().atStartOfDay());
        // watering code 계산
        int wateringCode = getWateringCode(period, prevPlant.getAverageWateringPeriod());

        // 해당 식물을 조회한 뒤, 물주기 정보를 업데이트할지 말지 결정 후 Plant를 돌려준다
        // 비료를 줬으면 Chemical, Plant를 포함하는 Watering 엔티티,
        // 맹물을 줬으면 Plant만 포함하는 엔티티
        Plant plant = getPlantForAdd(prevPlant, period);

        // 저장
        Watering watering = wateringRepository.save(getWateringEntity(wateringRequest, plant));

        // msg
        WateringDto.WateringMsg wateringMsg = new WateringDto.WateringMsg(wateringCode, plant.getAverageWateringPeriod());

        return WateringDto.WateringResponse.withWateringMsgFrom(watering, wateringMsg);
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

    @Override
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
    public List<WateringDto.WateringForOnePlant> modifyWatering(WateringDto.WateringRequest wateringRequest) {
        log.debug("wateringRequest: {}", wateringRequest);

        // Mapping할 Entity 가져오기
        // chemical은 nullable이므로 orElse 사용
        Plant plant = plantRepository.findById(wateringRequest.getPlantNo()).orElseThrow(NoSuchElementException::new);
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElse(null);

        // 기존 watering 엔티티
        Watering watering = wateringRepository.findById(wateringRequest.getWateringNo())
                .orElseThrow(NoSuchElementException::new);

        wateringRepository.save(watering.update(wateringRequest.getWateringDate(), plant, chemical));

        return getWateringListForPlant(wateringRequest.getPlantNo());
    }

    @Override
    public void deleteWatering(int wateringNo) {
        wateringRepository.deleteById(wateringNo);
    }
}

package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDateTime;
import java.util.List;

public interface WateringService {
    /* 이번 관수가 며칠만인지 계산 */
    int calculateWateringPeriod(LocalDateTime latestWateringDate);

    /* 저장할 식물 엔티티 (watering period 변경) */
    Plant getPlantForAdd(Plant plant, int period);

    /* 프론트에서 메시지를 산출할 때 쓸 코드 */
    int getWateringCode(int period, int prevWateringPeriod);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo);

    /* 물주기 기록 추가 */
    WateringDto.WateringModifyResponse addWatering(WateringDto.WateringRequest wateringRequest);

    /* 가공한 watering list 리턴 (ex. 며칠만에 물 줬는지) */
    List<WateringDto.WateringForOnePlant> withWateringPeriodList(List<Watering> list);

    /* 물주기 수정 */
    WateringDto.WateringModifyResponse modifyWatering(WateringDto.WateringRequest wateringRequest);

    /* 물주기 하나 삭제 */
    void deleteWatering(int wateringNo);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAllFromPlant(int plantNo);
}

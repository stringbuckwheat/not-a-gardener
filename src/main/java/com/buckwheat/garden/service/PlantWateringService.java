package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PlantWateringService {
    /* 이번 관수가 며칠만인지 계산 */
    int calculateWateringPeriod(LocalDateTime latestWateringDate);

    /* 물주기 기록 추가 */
    WateringDto.WateringModifyResponse addWatering(WateringDto.WateringRequest wateringRequest);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo);

    /* 물주기 수정 */
    WateringDto.WateringModifyResponse modifyWatering(WateringDto.WateringRequest wateringRequest);

    /* 물주기 하나 삭제 */
    void deleteWatering(int wateringNo);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAllFromPlant(int plantNo);
}

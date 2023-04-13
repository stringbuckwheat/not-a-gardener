package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDateTime;
import java.util.List;

public interface PlantWateringService {
    /* 이번 관수가 며칠만인지 계산 */
    int calculateWateringPeriod(LocalDateTime latestWateringDate);

    /* 물주기 기록 추가 */
    WateringDto.AfterWatering addWatering(WateringDto.Request wateringRequest);

    /* insert, update 이후 리턴할 DTO를 만드는 메소드 */
    WateringDto.AfterWatering getAfterWatering(Plant plant);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo);

    /* 물주기 간격을 포함한 WateringDto 리스트*/
    List<WateringDto.WateringForOnePlant> withWateringPeriodList(List<Watering> list);

    /* 물주기 수정 */
    WateringDto.AfterWatering modifyWatering(WateringDto.Request wateringRequest);

    /* 물주기 하나 삭제 */
    void deleteWatering(int wateringNo);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAllFromPlant(int plantNo);
}

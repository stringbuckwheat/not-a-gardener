package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import com.buckwheat.garden.data.dto.watering.WateringRequest;

import java.util.List;

public interface PlantWateringService {
    /* 물주기 기록 추가 */
    AfterWatering add(WateringRequest wateringRequest);

    /* insert, update 이후 리턴할 DTO를 만드는 메소드 */
    AfterWatering getAfterWatering(Long plantId);

    /* 한 식물의 물주기 기록 */
    List<WateringForOnePlant> getAll(Long plantId);

    /* 물주기 수정 */
    AfterWatering modify(WateringRequest wateringRequest);

    /* 물주기 하나 삭제 */
    void delete(Long wateringId);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAll(Long plantId);
}

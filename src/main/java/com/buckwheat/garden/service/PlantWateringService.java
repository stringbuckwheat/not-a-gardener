package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.util.List;

public interface PlantWateringService {
    /* 물주기 기록 추가 */
    WateringDto.AfterWatering add(WateringDto.Request wateringRequest);

    /* insert, update 이후 리턴할 DTO를 만드는 메소드 */
    WateringDto.AfterWatering getAfterWatering(Long plantId);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.ForOnePlant> getAll(Long plantId);

    /* 물주기 수정 */
    WateringDto.AfterWatering modify(WateringDto.Request wateringRequest);

    /* 물주기 하나 삭제 */
    void delete(Long wateringId);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAll(Long plantId);
}

package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.watering.PlantWateringResponse;
import com.buckwheat.garden.data.dto.watering.WateringForOnePlant;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlantWateringService {
    /* 물주기 기록 추가 */
    PlantWateringResponse add(WateringRequest wateringRequest, Pageable pageable);

    /* 한 식물의 물주기 기록 */
    List<WateringForOnePlant> getAll(Long plantId, Pageable pageable);

    /* 물주기 수정 */
    PlantWateringResponse modify(WateringRequest wateringRequest, Pageable pageable, Long gardenerId);

    /* 물주기 하나 삭제 */
    void delete(Long wateringId, Long plantId, Long gardenerId);

    /* 한 식물의 모든 물주기 삭제 */
    void deleteAll(Long plantId);
}

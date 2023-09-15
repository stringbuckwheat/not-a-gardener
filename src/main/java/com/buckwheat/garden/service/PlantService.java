package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantDetail;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.dto.plant.PlantResponse;

import java.util.List;

public interface PlantService {
    /* 식물 리스트 */
    List<PlantResponse> getAll(Long gardenerId);

    /* 하나의 식물 */
    PlantDetail getDetail(Long plantId, Long gardenerId);

    /* 식물 추가 */
    GardenResponse add(Long gardenerId, PlantRequest plantRequest);

    /* 식물 수정 */
    GardenResponse modify(Long gardenerId, PlantRequest plantRequest);

    /* 식물들의 장소 정보 수정 */
    PlaceDto modifyPlace(ModifyPlace modifyPlantPlace, Long gardenerId);

    /* 식물 삭제 */
    void delete(Long plantId, Long gardenerId);
}
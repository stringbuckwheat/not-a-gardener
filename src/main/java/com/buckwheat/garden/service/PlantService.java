package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.GardenDto;

import java.util.List;

public interface PlantService {
    /* 하나의 식물 */
    GardenDto getOnePlant(int plantNo);

    /* 식물 리스트 */
    List<PlantDto> getPlantList(int memberNo);

    /* 식물 추가 */
    void addPlant(PlantRequestDto plantRequestDto);

    /* 식물 수정 */
    void modifyPlant(PlantRequestDto plantRequestDto);

    /* 식물 삭제 */
    void deletePlantByPlantNo(int plantNo);
}

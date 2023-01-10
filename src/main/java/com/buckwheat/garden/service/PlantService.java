package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.PlantRequestDto;
import com.buckwheat.garden.data.dto.PlantDto;

public interface PlantService {
    /* 하나의 식물 */
    PlantDto getOnePlant(int plantNo);

    /* 식물 추가 */
    void addPlant(PlantRequestDto plantRequestDto);

    /* 식물 수정 */
    void modifyPlant(PlantRequestDto plantRequestDto);

    /* 식물 삭제 */
    void deletePlantByPlantNo(int plantNo);
}

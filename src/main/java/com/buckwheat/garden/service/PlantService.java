package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.ModifyPlantPlaceDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.List;

public interface PlantService {
    /* 하나의 식물 */
    PlantDto.PlantResponse getOnePlant(int plantNo);

    /* 식물 리스트 */
    List<PlantDto.PlantResponse> getPlantList(int memberNo);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringList> getWateringListForPlant(int plantNo);

    /* 식물 추가 */
    void addPlant(PlantDto.PlantRequest plantRequestDto, Member member);

    /* 식물 수정 */
    PlantDto.PlantResponse modifyPlant(PlantDto.PlantRequest plantRequestDto, Member member);

    /* 식물 삭제 */
    void deletePlantByPlantNo(int plantNo);

    /* 식물들의 장소 정보 수정 */
    void modifyPlantPlace(ModifyPlantPlaceDto modifyPlantPlaceDto);
}
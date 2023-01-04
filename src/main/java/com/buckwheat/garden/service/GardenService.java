package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.AddPlantDto;
import com.buckwheat.garden.data.dto.PlantDto;

import java.time.LocalDate;
import java.util.List;

public interface GardenService {
    /* 해당 유저의 전체 식물과 가장 최근 물/비료 준 날짜 리스트 반환 */
    List<PlantDto> getPlantList(String id);

    /* 해당 식물의 wateringCode와 fertilizingCode를 반환 */
    void calculateCode(PlantDto plantDto);

    /* 비료든 물이든 마지막으로 물 준 날짜 구하기 */
    LocalDate getLastDrinkingDay(int plantNo);

    /* 비료 시비 여부 */
    int getFertilizingCode(int plantNo);

    /* 식물 추가 */
    void addPlant(AddPlantDto addPlantDto);
}
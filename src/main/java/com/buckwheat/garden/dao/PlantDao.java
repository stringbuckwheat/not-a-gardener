package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    /* 해당 유저의 전체 식물 리스트 가져오기 */
    List<Plant> getPlantListByUsername(String id);
    Plant savePlant(Plant plant);

    /* 식물 하나의 정보 가져오기 */
    Plant getPlantOne(int plantNo);

    /* 최근 물주기 업데이트 */
    void updateAverageWateringPeriod(int plantNo, int avgWateringPeriod);
}

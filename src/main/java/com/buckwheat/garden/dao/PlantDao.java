package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface PlantDao {
    /* 해당 유저의 전체 식물 리스트 가져오기 */
    List<Plant> getPlantListByUsername(String id);
}

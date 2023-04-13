package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Watering;

import java.util.List;

public interface WateringDao {
    Watering addWatering(WateringDto.Request wateringRequest);
    List<Watering> getWateringListByPlantNo(int plantNo);
    Watering modifyWatering(WateringDto.Request wateringRequest);
    void removeWatering(int wateringNo);
    void removeAllWateringByPlantNo(int plantNo);
}

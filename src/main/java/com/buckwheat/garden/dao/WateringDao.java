package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDate;
import java.util.List;

public interface WateringDao {
    Watering addWatering(WateringDto.Request wateringRequest);
    List<Watering> getWateringListByPlantNo(int plantNo);
    List<Watering> getAllWateringListByMemberNo(int memberNo, LocalDate startDate, LocalDate endDate);
    Watering modifyWatering(WateringDto.Request wateringRequest);
    void removeWatering(int wateringNo);
    void removeAllWateringByPlantNo(int plantNo);
}

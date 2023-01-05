package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDate;
import java.util.List;

public interface WateringDao {
    /* 최근 물 준 날짜를 구하는 메소드 */
    LocalDate getLatestWateringDayByPlantNo(int plantNo);

    /* 최근 비료 준 날짜를 구하는 메소드 */
    LocalDate getLatestFertilizedDayByPlantNo(int plantNo);

    /* 물 주기 저장 */
    Watering addWatering(Watering watering);

    /* 최근 물주기 두 개 반환
    * plant.averageWateringPeriod 업데이트에 사용 */
    List<Watering> getRecentTwoWateringDays(int plantNo);
}

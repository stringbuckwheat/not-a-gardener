package com.buckwheat.garden.dao;

import java.time.LocalDate;

public interface WateringDao {
    /* 최근 물 준 날짜를 구하는 메소드 */
    LocalDate getLatestWateringDayByPlantNo(int plantNo);

    /* 최근 비료 준 날짜를 구하는 메소드 */
    LocalDate getLatestFertilizedDayByPlantNo(int plantNo);
}

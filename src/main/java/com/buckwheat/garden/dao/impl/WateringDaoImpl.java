package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.repository.WateringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WateringDaoImpl implements WateringDao {
    @Autowired
    private WateringRepository wateringRepository;

    @Override
    public LocalDate getLatestWateringDayByPlantNo(int plantNo){
        return wateringRepository.findLatestWateringDayByPlantNo(plantNo);
    }

    @Override
    public LocalDate getLatestFertilizedDayByPlantNo(int plantNo) {
        return wateringRepository.findLatestFertilizedDayByPlantNo(plantNo);
    }
}

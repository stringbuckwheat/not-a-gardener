package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.WateringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public Watering addWatering(Watering watering) {
        return wateringRepository.save(watering);
    }

    @Override
    public List<Watering> getRecentTwoWateringDays(int plantNo) {
        return wateringRepository.findTop2ByPlantNoOrderByWateringNoDesc(plantNo);
    }
}

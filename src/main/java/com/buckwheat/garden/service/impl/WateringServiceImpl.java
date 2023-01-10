package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.PlantDao;
import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.WaterDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.WateringService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

@Service
@Slf4j
public class WateringServiceImpl implements WateringService {
    @Autowired
    private WateringDao wateringDao;

    @Autowired
    private PlantDao plantDao;

    @Override
    public int addWatering(WaterDto waterDto) {

        wateringDao.addWatering(waterDto.toEntity());
        return getWateringPeriodCode(waterDto.getPlantNo());
    }

    @Override
    public int getWateringPeriodCode(int plantNo){
        // 이전 물주기와 비교
        int prevWateringPeriod = plantDao.getPlantOne(plantNo).getAverageWateringPeriod();

        // DB에서 마지막 물주기 row 2개 들고와서 주기 계산
        List<Watering> recentWateringList = wateringDao.getRecentTwoWateringDays(plantNo);
        log.debug("recentWateringList: " + recentWateringList);

        if(recentWateringList.size() == 1){
            // 물주기 기록이 오직 한 개인 경우
            // TODO plant 테이블의 create_date과 비교하거나, 데이터 두 개 모일 때까지 기다리거나!
            return 0;
        }

        int tmpPeriod = Period.between(recentWateringList.get(1).getWateringDate(), recentWateringList.get(0).getWateringDate()).getDays();

        if(prevWateringPeriod > tmpPeriod){
            // 물주기가 짧아진 경우 -> DB에 반영
            plantDao.updateAverageWateringPeriod(plantNo, tmpPeriod);
            return -1;
        } else if (prevWateringPeriod == tmpPeriod){
            // 물주기 똑같음
            return 0;
        } else {
            // 물주기 길어짐
            // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
            return 1;
        }
    }
}

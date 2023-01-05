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
    public String addWatering(WaterDto waterDto) {
        wateringDao.addWatering(waterDto.toEntity());

        // 이전 물주기와 비교
        int prevWateringPeriod = plantDao.getPlantOne(waterDto.getPlantNo()).getAverageWateringPeriod();

        // DB에서 마지막 물주기 row 2개 들고와서 주기 계산
        List<Watering> recentWateringList = wateringDao.getRecentTwoWateringDays(waterDto.getPlantNo());
        int tmpPeriod = Period.between(recentWateringList.get(1).getWateringDate(), recentWateringList.get(0).getWateringDate()).getDays();
        log.debug("tmpPeriod: " + tmpPeriod);

        String message = "";

        // 물주기가 짧아진 경우
        if(prevWateringPeriod > tmpPeriod){
            // 식물이 성장해서 물주기가 짧아짐
            message = "물 주기가 짧아졌어요! 식물이 잘 성장하고 있다는 뜻입니다!";

            // DB 업데이트
            plantDao.updateAverageWateringPeriod(waterDto.getPlantNo(), tmpPeriod);

        } else if (prevWateringPeriod < tmpPeriod){
            // 사람이 물 주기를 놓쳤거나 날씨가 안 좋아서 물이 늦게 마른 경우
            // 메시지만 보냄
            message = "물 주기가 길어졌어요! 물 줄 날짜를 놓쳤거나 날씨가 좋지 않았던 모양이에요.";

            // DB 업데이트 안 함
        }

        return message;
    }
}

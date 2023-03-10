package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.util.List;

public interface WateringService {

    int getWateringPeriodCode(int plantNo);


    List<WateringDto.WateringList> getWateringList(int memberNo);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo);

    WateringDto.WateringResponse addWatering(WateringDto.WateringRequest wateringRequest);

    WateringDto.WateringResponse modifyWatering(WateringDto.WateringRequest wateringRequest);

    void deleteWatering(int wateringNo);
}

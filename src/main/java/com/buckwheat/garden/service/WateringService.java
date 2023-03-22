package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.util.List;

public interface WateringService {

    List<WateringDto.WateringList> getWateringList(int memberNo);

    /* 한 식물의 물주기 기록 */
    List<WateringDto.WateringForOnePlant> getWateringListForPlant(int plantNo);

    WateringDto.WateringResponse addWatering(WateringDto.WateringRequest wateringRequest);

    List<WateringDto.WateringForOnePlant> modifyWatering(WateringDto.WateringRequest wateringRequest);

    void deleteWatering(int wateringNo);

    void deleteAllFromPlant(int plantNo);
}

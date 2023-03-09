package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.WateringDto;

import java.util.List;

public interface WateringService {

    int getWateringPeriodCode(int plantNo);


    //////
    List<WateringDto.WateringList> getWateringList(int memberNo);

    WateringDto.WateringResponse addWatering(WateringDto.WateringRequest wateringRequest);

    WateringDto.WateringResponse modifyWatering(WateringDto.WateringRequest wateringRequest);

    void deleteWatering(int wateringNo);
}

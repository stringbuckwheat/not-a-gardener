package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.place.Place;
import com.buckwheat.garden.domain.place.dto.ModifyPlace;
import com.buckwheat.garden.domain.plant.dto.plant.PlantRequest;

public interface PlantCommandService {
    <T> T save(Long gardenerId, PlantRequest plantRequest, ServiceCallBack<T> callBack);

    <T> T update(PlantRequest plantRequest, Long gardenerId, ServiceCallBack<T> callBack);

    Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId);
}

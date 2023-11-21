package com.buckwheat.garden.plant;

import com.buckwheat.garden.data.entity.Place;
import com.buckwheat.garden.place.dto.ModifyPlace;
import com.buckwheat.garden.plant.plant.PlantRequest;

public interface PlantCommandService {
    <T> T save(Long gardenerId, PlantRequest plantRequest, ServiceCallBack<T> callBack);

    <T> T update(PlantRequest plantRequest, Long gardenerId, ServiceCallBack<T> callBack);

    Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId);
}

package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.place.Place;
import xyz.notagardener.domain.place.dto.ModifyPlace;
import xyz.notagardener.domain.plant.dto.plant.PlantRequest;

public interface PlantCommandService {
    <T> T save(Long gardenerId, PlantRequest plantRequest, ServiceCallBack<T> callBack);

    <T> T update(PlantRequest plantRequest, Long gardenerId, ServiceCallBack<T> callBack);

    Place updatePlantPlace(ModifyPlace modifyPlantPlace, Long gardenerId);
}

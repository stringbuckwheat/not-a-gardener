package xyz.notagardener.repot.service;

import xyz.notagardener.plant.Plant;
import xyz.notagardener.repot.dto.RepotRequest;
import xyz.notagardener.status.PlantStatusResponse;

public interface RepotStatusService {
    PlantStatusResponse handleRepotStatus(RepotRequest request, Plant plant);
}

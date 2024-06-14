package xyz.notagardener.status.plant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.status.common.model.StatusEntities;

@NoArgsConstructor
@Getter
public class AddStatusResponse {
    private PlantStatusResponse status;
    private StatusLogResponse statusLog;

    public AddStatusResponse(StatusEntities entities) {
        this.status = new PlantStatusResponse(entities.getStatus());
        this.statusLog = new StatusLogResponse(entities.getStatusLog());
    }
}

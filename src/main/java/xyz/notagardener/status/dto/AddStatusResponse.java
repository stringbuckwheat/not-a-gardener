package xyz.notagardener.status.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.status.model.Status;
import xyz.notagardener.status.model.StatusLog;

@NoArgsConstructor
@Getter
public class AddStatusResponse {
    private PlantStatusResponse status;
    private StatusLogResponse statusLog;

    public AddStatusResponse(Status status, StatusLog statusLog) {
        this.status = new PlantStatusResponse(status);
        this.statusLog = new StatusLogResponse(statusLog);
    }
}

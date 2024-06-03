package xyz.notagardener.status.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@ToString
@Getter
public class SimplePlantStatus {
    private String status;
    private LocalDate recordedDate; // max 값

    @QueryProjection
    public SimplePlantStatus(String status, LocalDate recordedDate) {
        this.status = status;
        this.recordedDate = recordedDate;
    }
}

package com.buckwheat.garden.domain.plant.dto.garden;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WaitingForWatering {
    private Long id;
    private String name;
    private String species;
    private String medium;
    private Long placeId;
    private String placeName;
    private LocalDate createDate;

    @QueryProjection
    public WaitingForWatering(Long id, String name, String species, String medium, Long placeId, String placeName, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.medium = medium;
        this.placeId = placeId;
        this.placeName = placeName;
        this.createDate = createDate.toLocalDate();
    }
}

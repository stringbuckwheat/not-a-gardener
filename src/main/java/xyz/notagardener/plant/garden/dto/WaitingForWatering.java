package xyz.notagardener.plant.garden.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class WaitingForWatering {
    @Schema(description = "식물 id", example = "6")
    private Long id;

    @Schema(description = "식물 이름", example = "아기 온시디움")
    private String name;

    @Schema(description = "식물 이름", example = "온시디움 트윙클")
    private String species;

    @Schema(description = "식재 환경", example = "수태")
    private String medium;

    @Schema(description = "장소 id", example = "4")
    private Long placeId;

    @Schema(description = "장소 이름", example = "창가")
    private String placeName;

    @Schema(description = "등록일", example = "2022-10-02")
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

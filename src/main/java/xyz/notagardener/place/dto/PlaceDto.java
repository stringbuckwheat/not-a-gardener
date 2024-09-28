package xyz.notagardener.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.place.model.Place;

import java.time.LocalDateTime;


/* Place INSERT, UPDATE 요청 시에 사용할 DTO */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class PlaceDto {
    @Schema(description = "장소 id", example = "1")
    private Long id;

    @Schema(description = "장소 이름", example = "창가")
    @NotBlank(message = "장소 이름은 비워둘 수 없어요")
    private String name;

    @Schema(description = "식물등 사용 여부", example = "Y")
    @NotBlank(message = "식물등 사용 여부는 비워둘 수 없어요")
    private String artificialLight;

    @Schema(description = "장소 타입", example = "실내")
    @NotBlank(message = "장소 타입은 비워둘 수 없어요")
    private String option;

    @Schema(description = "장소에 속한 식물 수", example = "6")
    private Long plantListSize;

    public Place toEntityWith(Gardener gardener) {
        return Place.builder()
                .placeId(id)
                .name(name)
                .artificialLight(artificialLight)
                .option(option)
                .createDate(LocalDateTime.now())
                .gardener(gardener)
                .build();
    }

    public PlaceDto(Place place) {
        this.id = place.getPlaceId();
        this.name = place.getName();
        this.artificialLight = place.getArtificialLight();
        this.option = place.getOption();
    }

    public PlaceDto(Place place, Long plantListSize) {
        this(place);
        this.plantListSize = plantListSize;
    }
}

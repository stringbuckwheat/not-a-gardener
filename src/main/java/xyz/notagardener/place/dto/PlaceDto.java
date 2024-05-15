package xyz.notagardener.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.place.PlaceType;

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
    @NotBlank
    private String name;

    @Schema(description = "식물등 사용 여부", example = "Y")
    @NotBlank
    private String artificialLight;

    @Schema(description = "장소 타입", example = "실내")
    @NotBlank
    private String option;

    @Schema(description = "장소에 속한 식물 수", example = "6")
    private Long plantListSize;

    public boolean isValidForSave() {
        boolean artificialLightValid = artificialLight != null && (artificialLight.equals("Y") || artificialLight.equals("N"));
        return name != null && name.length() > 0 && name.length() < 50 && PlaceType.isValid(option) && artificialLightValid;
    }

    public boolean isValidForUpdate() {
        return id != null && id > 0L && isValidForSave();
    }

    /**
     * createDate로 쓸 LocalDateTime.now()를 포함한 엔티티를 반환
     *
     * @param gardener FK 매핑
     * @return Place
     */
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

    public static PlaceDto from(Place place) {
        return PlaceDto.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .build();
    }

    public static PlaceDto from(Place place, Long plantListSize) {
        return PlaceDto.builder()
                .id(place.getPlaceId())
                .name(place.getName())
                .artificialLight(place.getArtificialLight())
                .option(place.getOption())
                .plantListSize(plantListSize)
                .build();
    }
}

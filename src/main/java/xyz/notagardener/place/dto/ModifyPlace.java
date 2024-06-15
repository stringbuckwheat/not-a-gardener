package xyz.notagardener.place.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPlace {
    @Schema(description = "장소 id", example = "9")
    @NotNull(message = "장소 이름은 비워둘 수 없어요")
    private Long placeId;

    @NotNull(message = "식물 정보는 비워둘 수 없어요")
    @Schema(description = "식물 id들", example = "[1, 2, 3]")
    private List<Long> plants;
}

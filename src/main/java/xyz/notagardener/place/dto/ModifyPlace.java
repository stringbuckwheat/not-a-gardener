package xyz.notagardener.place.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ModifyPlace {
    @NotNull(message = "장소 이름은 비워둘 수 없어요")
    private Long placeId;

    @NotNull(message = "식물 정보는 비워둘 수 없어요")
    private List<Long> plants;
}

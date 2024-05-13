package xyz.notagardener.domain.place.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ModifyPlace {
    @NotNull
    private Long placeId;

    @NotNull
    private List<Long> plants;
}

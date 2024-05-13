package xyz.notagardener.domain.gardener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Refresh {
    @NotNull
    private Long gardenerId;

    @NotBlank
    private String refreshToken;
}

package xyz.notagardener.gardener.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Refresh {
    @NotNull
    private Long gardenerId;

    @NotBlank
    private String refreshToken;
}

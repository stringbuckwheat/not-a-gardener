package xyz.notagardener.chemical.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.gardener.Gardener;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ChemicalDto {
    @Schema(description = "약품 id", example = "1")
    private Long id;

    @NotBlank(message = "약품 이름은 필수 입력값입니다.")
    @Schema(description = "약품 이름", example = "하이포넥스")
    private String name;

    @NotBlank(message = "약품 분류는 필수 입력값입니다.")
    @Schema(description = "약품 분류", example = "기본 NPK 비료")
    private String type;

    @NotNull(message = "시비 주기는 필수 입력값입니다.")
    @Positive(message = "시비 주기는 양수만 입력 가능합니다.")
    @Schema(description = "시비 주기", example = "7")
    private int period;

    @Builder
    @QueryProjection
    public ChemicalDto(Long id, String name, String type, int period) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.period = period;
    }

    public static ChemicalDto from(Chemical chemical) {
        return ChemicalDto.builder()
                .id(chemical.getChemicalId())
                .name(chemical.getName())
                .type(chemical.getType())
                .period(chemical.getPeriod())
                .build();
    }

    public Chemical toEntity(Gardener gardener) {
        return Chemical.builder()
                .name(name)
                .period(period)
                .type(type)
                .gardener(gardener)
                .active("Y")
                .build();
    }

    public Chemical toEntityForUpdate(Gardener gardener) {
        return Chemical.builder()
                .chemicalId(id)
                .name(name)
                .period(period)
                .type(type)
                .gardener(gardener)
                .active("Y")
                .build();
    }
}


package xyz.notagardener.chemical.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.common.validation.ChemicalTypeConstraints;
import xyz.notagardener.gardener.Gardener;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class ChemicalDto {
    @Schema(description = "약품 id", example = "1")
    private Long id;

    @NotBlank(message = "약품 이름을 입력해주세요.")
    @Size(max = 30, message = "약품 이름은 30자를 넘을 수 없어요.")
    @Schema(description = "약품 이름", example = "하이포넥스")
    private String name;

    @NotBlank(message = "약품 분류를 입력해주세요.")
    @ChemicalTypeConstraints(message = "약품 분류를 확인해주세요.")
    @Schema(description = "약품 분류", example = "기본 NPK 비료")
    private String type;

    @NotNull(message = "시비 주기를 입력해주세요.")
    @Positive(message = "시비 주기는 양수만 입력 가능해요.")
    @Schema(description = "시비 주기", example = "7")
    private Integer period;

    @QueryProjection
    public ChemicalDto(Long id, String name, String type, int period) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.period = period;
    }

    public ChemicalDto (Chemical chemical) {
        this.id = chemical.getChemicalId();
        this.name = chemical.getName();
        this.type = chemical.getType();
        this.period = chemical.getPeriod();
    }

    public Chemical toEntityWith(Gardener gardener) {
        return Chemical.builder()
                .name(name)
                .period(period)
                .type(type)
                .gardener(gardener)
                .active("Y")
                .build();
    }
}


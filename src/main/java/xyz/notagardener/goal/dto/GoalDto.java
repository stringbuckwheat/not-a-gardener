package xyz.notagardener.goal.dto;

import jakarta.validation.constraints.Size;
import xyz.notagardener.common.validation.YesOrNo;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.goal.Goal;
import xyz.notagardener.plant.Plant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GoalDto {
    @Schema(description = "목표 id", example = "1")
    private Long id;

    @Schema(description = "목표 내용", example = "사라왁 꽃 보기")
    @Size(min = 1, max = 100)
    @NotBlank
    private String content;

    @Schema(description = "달성 여부", example = "N")
    @YesOrNo
    private YesOrNoType complete;

    @Schema(description = "식물 id", example = "8") // nullable
    private Long plantId;

    @Schema(description = "식물 이름", example = "베고니아 사라왁")
    private String plantName;

    public Goal toEntityWith(Gardener gardener, Plant plant) {
        return Goal.builder()
                .goalId(id)
                .content(content)
                .complete(complete)
                .gardener(gardener)
                .plant(plant)
                .build();
    }

    public GoalDto(Goal goal) {
        this.id = goal.getGoalId();
        this.content = goal.getContent();
        this.complete = goal.getComplete();

        if (goal.getPlant() != null) {
            this.plantId = goal.getPlant().getPlantId();
            this.plantName = goal.getPlant().getName();
        }
    }
}


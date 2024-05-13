package xyz.notagardener.domain.goal;

import xyz.notagardener.domain.gardener.Gardener;
import xyz.notagardener.domain.plant.Plant;
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
    @NotBlank
    private String content;

    @Schema(description = "달성 여부", example = "N")
    private String complete;

    @Schema(description = "식물 id", example = "8")
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

    public static GoalDto from(Goal goal) {
        // plant == nullable
        if (goal.getPlant() != null) {
            return GoalDto.builder()
                    .id(goal.getGoalId())
                    .content(goal.getContent())
                    .complete(goal.getComplete())
                    .plantId(goal.getPlant().getPlantId())
                    .plantName(goal.getPlant().getName())
                    .build();
        }

        return GoalDto.builder()
                .id(goal.getGoalId())
                .content(goal.getContent())
                .complete(goal.getComplete())
                .build();
    }
}


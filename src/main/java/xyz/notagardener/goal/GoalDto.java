package xyz.notagardener.goal;

import xyz.notagardener.gardener.Gardener;
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
    @NotBlank
    private String content;

    @Schema(description = "달성 여부", example = "N")
    private String complete;

    @Schema(description = "식물 id", example = "8")
    private Long plantId;

    @Schema(description = "식물 이름", example = "베고니아 사라왁")
    private String plantName;

    public boolean isValidForSave() {
        return content != null
                && content.length() > 0
                && content.length() < 80
                && complete != null
                && complete.equals("N")
                && plantId >= 0;
    }

    public boolean isValidForUpdate() {
        return id != null
                && id > 0
                && content != null
                && content.length() > 0
                && content.length() < 50
                && complete != null
                && (complete.equals("Y") || complete.equals("N"))
                && plantId >= 0;
    }

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
        GoalDtoBuilder builder = GoalDto.builder()
                .id(goal.getGoalId())
                .content(goal.getContent())
                .complete(goal.getComplete());

        if (goal.getPlant() != null) {
            builder.plantId(goal.getPlant().getPlantId())
                    .plantName(goal.getPlant().getName());
        }

        return builder.build();
    }
}


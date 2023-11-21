package com.buckwheat.garden.goal;

import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;


@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GoalDto {
    private Long id;
    private String content;
    private String complete;
    private Long plantId;
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


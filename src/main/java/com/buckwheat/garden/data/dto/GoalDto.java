package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

public class GoalDto {
    @Getter
    @ToString
    public static class Request {
        private Long id;
        private String content;
        private String complete;
        private Long plantId;

        public Goal toEntityWith(Member member, Plant plant) {
            return Goal.builder()
                    .goalId(id)
                    .content(content)
                    .complete(complete)
                    .member(member)
                    .plant(plant)
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Response {
        private Long id;
        private String content;
        private String complete;
        private Long plantId;
        private String plantName;

        public static Response from(Goal goal) {
            // plant == nullable
            if (goal.getPlant() != null) {
                return Response.builder()
                        .id(goal.getGoalId())
                        .content(goal.getContent())
                        .complete(goal.getComplete())
                        .plantId(goal.getPlant().getPlantId())
                        .plantName(goal.getPlant().getName())
                        .build();
            }

            return Response.builder()
                    .id(goal.getGoalId())
                    .content(goal.getContent())
                    .complete(goal.getComplete())
                    .build();
        }

        public static Response from(Goal goal, Plant plant) {
            // plant == nullable
            if (plant != null) {
                return Response.builder()
                        .id(goal.getGoalId())
                        .content(goal.getContent())
                        .complete(goal.getComplete())
                        .plantId(plant.getPlantId())
                        .plantName(plant.getName())
                        .build();
            }

            return Response.builder()
                    .id(goal.getGoalId())
                    .content(goal.getContent())
                    .complete(goal.getComplete())
                    .build();
        }
    }

}

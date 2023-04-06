package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

public class GoalDto {
    @Getter
    @ToString
    public static class Request {
        private int goalNo;
        private String goalContent;
        private String complete;
        private int plantNo;

        public Goal toEntityWith(Member member, Plant plant) {
            return Goal.builder()
                    .goalNo(goalNo)
                    .goalContent(goalContent)
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
        private int goalNo;
        private String goalContent;
        private String complete;
        private int plantNo;
        private String plantName;

        public static Response from(Goal goal) {
            // plant == nullable
            if (goal.getPlant() != null) {
                return Response.builder()
                        .goalNo(goal.getGoalNo())
                        .goalContent(goal.getGoalContent())
                        .complete(goal.getComplete())
                        .plantNo(goal.getPlant().getPlantNo())
                        .plantName(goal.getPlant().getPlantName())
                        .build();
            }

            return Response.builder()
                    .goalNo(goal.getGoalNo())
                    .goalContent(goal.getGoalContent())
                    .complete(goal.getComplete())
                    .build();
        }

        public static Response from(Goal goal, Plant plant) {
            // plant == nullable
            if (plant != null) {
                return Response.builder()
                        .goalNo(goal.getGoalNo())
                        .goalContent(goal.getGoalContent())
                        .complete(goal.getComplete())
                        .plantNo(plant.getPlantNo())
                        .plantName(plant.getPlantName())
                        .build();
            }

            return Response.builder()
                    .goalNo(goal.getGoalNo())
                    .goalContent(goal.getGoalContent())
                    .complete(goal.getComplete())
                    .build();
        }
    }

}

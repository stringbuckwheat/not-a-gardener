package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Goal;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import lombok.*;

public class GoalDto {
//    @Getter
//    @ToString
//    public static class Request {
//        private Long id;
//        private String content;
//        private String complete;
//        private Long plantId;
//
//        public Goal toEntityWith(Gardener gardener, Plant plant) {
//            return Goal.builder()
//                    .goalId(id)
//                    .content(content)
//                    .complete(complete)
//                    .gardener(gardener)
//                    .plant(plant)
//                    .build();
//        }
//    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static class Basic {
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

        public static Basic from(Goal goal) {
            // plant == nullable
            if (goal.getPlant() != null) {
                return Basic.builder()
                        .id(goal.getGoalId())
                        .content(goal.getContent())
                        .complete(goal.getComplete())
                        .plantId(goal.getPlant().getPlantId())
                        .plantName(goal.getPlant().getName())
                        .build();
            }

            return Basic.builder()
                    .id(goal.getGoalId())
                    .content(goal.getContent())
                    .complete(goal.getComplete())
                    .build();
        }
    }

}

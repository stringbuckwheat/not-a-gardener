package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class RoutineDto {
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class Main{
        List<Response> todoList;
        List<Response> notToDoList;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    public static class Response {
        private Long id;
        private String content;
        private int cycle;
        private Long plantId;
        private String plantName;
        private LocalDate lastCompleteDate;

        // 계산해서 넣는 값
        private String hasToDoToday;
        private String isCompleted;

        public static Response from(Routine routine, String hasToDoToday, String isCompleted){
            return Response.builder()
                    .id(routine.getRoutineId())
                    .content(routine.getContent())
                    .cycle(routine.getCycle())
                    .plantId(routine.getPlant().getPlantId())
                    .plantName(routine.getPlant().getName())
                    .lastCompleteDate(routine.getLastCompleteDate())
                    .hasToDoToday(hasToDoToday)
                    .isCompleted(isCompleted)
                    .build();
        }

        public static Response from(Routine routine, Plant plant, String hasToDoToday, String isCompleted){
            return Response.builder()
                    .id(routine.getRoutineId())
                    .content(routine.getContent())
                    .cycle(routine.getCycle())
                    .plantId(plant.getPlantId())
                    .plantName(plant.getName())
                    .lastCompleteDate(routine.getLastCompleteDate())
                    .hasToDoToday(hasToDoToday)
                    .isCompleted(isCompleted)
                    .build();
        }
    }

    @Getter
    @ToString
    public static class Request{
        private Long id;
        private String content;
        private int cycle;
        private Long plantId;

        public Routine toEntityWith(Plant plant, Gardener gardener){
            return Routine.builder()
                    .routineId(id)
                    .content(content)
                    .cycle(cycle)
                    .plant(plant)
                    .gardener(gardener)
                    .createDate(LocalDate.now())
                    .build();
        }
    }

    @Getter
    @ToString
    public static class Complete{
        private Long id;
        private LocalDate lastCompleteDate;
    }
}

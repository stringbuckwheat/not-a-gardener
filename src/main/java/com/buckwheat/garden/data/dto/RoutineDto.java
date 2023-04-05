package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class RoutineDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class Main{
        List<Response> todoList;
        List<Response> notToDoList;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Response {
        private int routineNo;
        private String routineContent;
        private int routineCycle;
        private int plantNo;
        private String plantName;
        private LocalDate lastCompleteDate;

        // 계산해서 넣는 값
        private String hasToDoToday;
        private String isCompleted;

        public static Response from(Routine routine, String hasToDoToday, String isCompleted){
            return Response.builder()
                    .routineNo(routine.getRoutineNo())
                    .routineContent(routine.getRoutineContent())
                    .routineCycle(routine.getRoutineCycle())
                    .plantNo(routine.getPlant().getPlantNo())
                    .plantName(routine.getPlant().getPlantName())
                    .lastCompleteDate(routine.getLastCompleteDate())
                    .hasToDoToday(hasToDoToday)
                    .isCompleted(isCompleted)
                    .build();
        }

        public static Response from(Routine routine, Plant plant, String hasToDoToday, String isCompleted){
            return Response.builder()
                    .routineNo(routine.getRoutineNo())
                    .routineContent(routine.getRoutineContent())
                    .routineCycle(routine.getRoutineCycle())
                    .plantNo(plant.getPlantNo())
                    .plantName(plant.getPlantName())
                    .lastCompleteDate(routine.getLastCompleteDate())
                    .hasToDoToday(hasToDoToday)
                    .isCompleted(isCompleted)
                    .build();
        }
    }

    @Getter
    @ToString
    public static class Request{
        private int routineNo;
        private String routineContent;
        private int routineCycle;
        private int plantNo;

        public Routine toEntityWith(Plant plant, Member member){
            return Routine.builder()
                    .routineNo(routineNo)
                    .routineContent(routineContent)
                    .routineCycle(routineCycle)
                    .plant(plant)
                    .member(member)
                    .createDate(LocalDate.now())
                    .build();
        }
    }

    @Getter
    @ToString
    public static class Complete{
        private int routineNo;
        private LocalDate lastCompleteDate;
    }
}

package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        public static Response from(Routine routine){
            LocalDate today = LocalDateTime.now().toLocalDate();

            return Response.builder()
                    .id(routine.getRoutineId())
                    .content(routine.getContent())
                    .cycle(routine.getCycle())
                    .plantId(routine.getPlant().getPlantId())
                    .plantName(routine.getPlant().getName())
                    .lastCompleteDate(routine.getLastCompleteDate())
                    .hasToDoToday(hasToDoToday(LocalDateTime.now(), routine))
                    .isCompleted(isCompleted(today, routine.getLastCompleteDate()))
                    .build();
        }

        public static String isCompleted(LocalDate today, LocalDate lastCompleteDate){
            // 한 번도 완료한 적 없는 루틴
            if(lastCompleteDate == null){
                return "N";
            }

            return lastCompleteDate.compareTo(today) == 0 ? "Y" : "N";
        }

        public static String hasToDoToday(LocalDateTime today, Routine routine) {
            // 한번도 완료하지 않은 루틴
            if (routine.getLastCompleteDate() == null) {
                return "Y";
            }

            // 완료한지 얼마나 지났는지 계산
            int period = (int) Duration.between(routine.getLastCompleteDate().atStartOfDay(), today).toDays();

            // 오늘 했거나 할 주기가 돌아왔으면 Y
            return (period == 0 || period >= routine.getCycle()) ? "Y" : "N";
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

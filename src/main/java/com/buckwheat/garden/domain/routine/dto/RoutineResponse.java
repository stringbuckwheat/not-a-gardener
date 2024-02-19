package com.buckwheat.garden.domain.routine.dto;

import com.buckwheat.garden.domain.routine.Routine;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Slf4j
public class RoutineResponse {
    @Schema(description = "루틴 id", example = "1")
    private Long id;

    @Schema(description = "루틴 내용", example = "아디안텀 1일 1관수!")
    private String content;

    @Schema(description = "루틴 주기", example = "1")
    private int cycle;

    @Schema(description = "루틴 해당 식물 id", example = "2")
    private Long plantId;

    @Schema(description = "루틴 해당 식물 이름", example = "아디안텀")
    private String plantName;

    @Schema(description = "가장 최근 완료한 날", example = "2024-02-01")
    private LocalDate lastCompleteDate;

    // 계산해서 넣는 값
    @Schema(description = "오늘 해야하는 일인지", example = "Y")
    private String hasToDoToday;

    @Schema(description = "완료된 상태인지", example = "N")
    private String isCompleted;

    public static RoutineResponse from(Routine routine) {
        LocalDate today = LocalDateTime.now().toLocalDate();

        return RoutineResponse.builder()
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

    public static String isCompleted(LocalDate today, LocalDate lastCompleteDate) {
        // 한 번도 완료한 적 없는 루틴
        if (lastCompleteDate == null) {
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


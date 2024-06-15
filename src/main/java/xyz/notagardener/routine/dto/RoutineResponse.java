package xyz.notagardener.routine.dto;

import xyz.notagardener.routine.Routine;
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

    @Schema(description = "루틴 반복 주기", example = "1")
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

    public RoutineResponse(Routine routine) {
        LocalDate today = LocalDateTime.now().toLocalDate();

        this.id = routine.getRoutineId();
        this.content = routine.getContent();
        this.cycle = routine.getCycle();
        this.plantId = routine.getPlant().getPlantId();
        this.plantName = routine.getPlant().getName();
        this.lastCompleteDate = routine.getLastCompleteDate();
        this.hasToDoToday = hasToDoToday(routine.getLastCompleteDate(), routine.getCycle()) ? "Y" : "N";
        this.isCompleted = isCompleted(today, routine.getLastCompleteDate());
    }

    private String isCompleted(LocalDate today, LocalDate lastCompleteDate) {
        // 한 번도 완료한 적 없는 루틴
        if (lastCompleteDate == null) {
            return "N";
        }

        return lastCompleteDate.compareTo(today) == 0 ? "Y" : "N";
    }

    private boolean hasToDoToday(LocalDate lastCompleteDate, int cycle) {
        // 한번도 완료하지 않은 루틴
        if (lastCompleteDate == null) {
            return true;
        }

        // 완료한지 얼마나 지났는지 계산
        int period = (int) Duration.between(lastCompleteDate.atStartOfDay(), LocalDateTime.now()).toDays();

        // 오늘 했거나 할 주기가 돌아왔으면 Y
        return (period == 0 || period >= cycle);
    }
}


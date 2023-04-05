package com.buckwheat.garden.util;

import com.buckwheat.garden.data.entity.Routine;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class RoutineUtil {
    public String hasToDoToday(Routine routine, LocalDateTime today) {
        // 한번도 완료하지 않은 루틴
        if (routine.getLastCompleteDate() == null) {
            return "Y";
        }

        // 완료한지 얼마나 지났는지 계산
        int period = (int) Duration.between(routine.getLastCompleteDate().atStartOfDay(), today).toDays();

        // 오늘 했거나 할 주기가 돌아왔으면 Y
        return (period == 0 || period >= routine.getRoutineCycle()) ? "Y" : "N";
    }

    public String isCompleted(LocalDate lastCompleteDate, LocalDateTime today){
        // 한 번도 완료한 적 없는 루틴
        if(lastCompleteDate == null){
            return "N";
        }

        return lastCompleteDate.compareTo(today.toLocalDate()) == 0 ? "Y" : "N";
    }
}

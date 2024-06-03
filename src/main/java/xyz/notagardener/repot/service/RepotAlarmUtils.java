package xyz.notagardener.repot.service;

import java.time.LocalDate;

public interface RepotAlarmUtils {
    boolean isRepotNeeded(int recentWateringPeriod, int earlyWateringPeriod, String status, LocalDate createDate);
}

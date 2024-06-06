package xyz.notagardener.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.status.dto.PlantStatusResponse;
import xyz.notagardener.status.dto.PlantStatusType;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RepotAlarmUtilsImpl implements RepotAlarmUtils {

    @Override
    public boolean isRepotNeeded(PlantResponse plantResponse) {
        return isRepotNeeded(plantResponse.getRecentWateringPeriod(), plantResponse.getEarlyWateringPeriod(), plantResponse.getStatus(), plantResponse.getCreateDate());
    }

    @Override
    public boolean isRepotNeeded(int recentWateringPeriod, int earlyWateringPeriod, List<PlantStatusResponse> status, LocalDate lastRepottedDate) {
        // 11월 ~ 3월이거나, 물 주기 간격 확보 안 됐거나, 최근 분갈이한 식물
        if (isTooColdToRepot() || isNotEnoughRecord(recentWateringPeriod, earlyWateringPeriod) || isRepottedRecently(status)) {
            return false;
        }

        return needsRepotting(recentWateringPeriod, earlyWateringPeriod, status, lastRepottedDate);
    }

    private boolean needsRepotting(int recentWateringPeriod, int earlyWateringPeriod, List<PlantStatusResponse> status, LocalDate lastRepottedDate) {
        if (isHeavyDrinker(status)) {
            return isPastOneYear(lastRepottedDate);
        }

        // 마지막 분갈이 이후 1년이 지났으면 분갈이 알림
        if (isPastOneYear(lastRepottedDate)) {
            // 며칠 지났는지 알려줄까?
            return true;
        }

        return isWateringPeriodReduced(earlyWateringPeriod, recentWateringPeriod);
    }

    private boolean isHeavyDrinker(List<PlantStatusResponse> statusList) {
        if (statusList == null) {
            return false;
        }

        for (PlantStatusResponse status : statusList) {
            if (PlantStatusType.HEAVY_DRINKER.getType().equals(status.getStatus())) {
                return true;
            }
        }

        return false;
    }

    private boolean isPastOneYear(LocalDate lastRepottedDate) {
        return lastRepottedDate.plusYears(1).isBefore(LocalDate.now());
    }

    private boolean isWateringPeriodReduced(int earlyWateringPeriod, int recentWateringPeriod) {
        int floor = (int) Math.floor((double) earlyWateringPeriod / recentWateringPeriod);

        // 물주기 롱텀
        if (earlyWateringPeriod > 21) {
            return floor >= 3;
        } else {
            return floor >= 2;
        }
    }

    private boolean isTooColdToRepot() {
        int thisMonth = LocalDate.now().getMonthValue();
        return thisMonth >= 11 || thisMonth <= 3;
    }

    private boolean isNotEnoughRecord(int recentWateringPeriod, int earlyWateringPeriod) {
        return recentWateringPeriod == 0 || earlyWateringPeriod == 0;
    }

    private boolean isRepottedRecently(List<PlantStatusResponse> statusList) {
        if (statusList == null) {
            return false;
        }

        for (PlantStatusResponse status : statusList) {
            if (PlantStatusType.JUST_REPOTTED.getType().equals(status.getStatus())) {
                return true;
            }
        }

        return false;
    }
}

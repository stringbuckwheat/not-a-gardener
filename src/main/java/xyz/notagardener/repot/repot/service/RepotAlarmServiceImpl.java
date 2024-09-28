package xyz.notagardener.repot.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.repot.model.Repot;
import xyz.notagardener.repot.repot.repository.RepotRepository;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RepotAlarmServiceImpl implements RepotAlarmService {
    private final RepotRepository repotRepository;

    @Override
    public boolean isRepotNeeded(PlantResponse plantResponse) {
        return isRepotNeeded(
                plantResponse.getId(),
                plantResponse.getRecentWateringPeriod(),
                plantResponse.getEarlyWateringPeriod(),
                plantResponse.getStatus().getHeavyDrinker(),
                plantResponse.getCreateDate()
        );
    }

    @Override
    public boolean isRepotNeeded(Long plantId, int recentWateringPeriod, int earlyWateringPeriod, YesOrNoType heavyDrinker, LocalDate createDate) {
        LocalDate lastRepottedDate = getLastRepottedDate(plantId, createDate);

        // 11월 ~ 3월이거나, 물 주기 간격 확보 안 됐거나, 최근 분갈이한 식물
        if (isTooColdToRepot() || isNotEnoughRecord(recentWateringPeriod, earlyWateringPeriod) || isRepottedRecently(lastRepottedDate)) {
            return false;
        }

        return needsRepotting(recentWateringPeriod, earlyWateringPeriod, heavyDrinker, lastRepottedDate);
    }

    private LocalDate getLastRepottedDate(Long plantId, LocalDate createDate) {
        // 가장 최근 분갈이 기록
        Optional<Repot> repot = repotRepository.findTopByPlant_PlantIdOrderByRepotDateDesc(plantId);

        if (repot.isEmpty()) {
            return createDate;
        }

        return repot.get().getRepotDate();
    }

    private boolean isRepottedRecently(LocalDate lastRepottedDate) {
        return lastRepottedDate.plusDays(14).isAfter(LocalDate.now());
    }

    private boolean needsRepotting(int recentWateringPeriod, int earlyWateringPeriod, YesOrNoType heavyDrinker, LocalDate lastRepottedDate) {
        // 헤비드링커인지
        if (YesOrNoType.Y.equals(heavyDrinker)) {
            return isPastOneYear(lastRepottedDate);
        }

        // 마지막 분갈이 이후 1년이 지났으면 분갈이 알림
        if (isPastOneYear(lastRepottedDate)) {
            // 며칠 지났는지 알려줄까?
            return true;
        }

        return isWateringPeriodReduced(earlyWateringPeriod, recentWateringPeriod);
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
}

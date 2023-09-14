package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.service.WateringService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.buckwheat.garden.data.entity.QChemical.chemical;
import static com.buckwheat.garden.data.entity.QPlant.plant;
import static com.buckwheat.garden.data.entity.QWatering.watering;

@SpringBootTest
class WateringRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    WateringRepository wateringRepository;
    @Autowired
    WateringService wateringService;

    @Test
    @DisplayName("물주기 달력 메소드 - 통과")
    void findAllWateringListByGardenerId() {
        // 파라미터 만들기
        LocalDate date = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);

        LocalDate originStartDate = firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7);

        int tmp = 42 - firstDayOfMonth.lengthOfMonth() - firstDayOfMonth.getDayOfWeek().getValue() % 7;
        LocalDate originEndDate =  firstDayOfMonth.plusDays(firstDayOfMonth.lengthOfMonth() - 1 + tmp);

        // 원본 메소드
        List<Watering> waterings = wateringRepository.findAllWateringListByGardenerId((long) 1, originStartDate, originEndDate);

        // 리팩토링 시작
        LocalDate startDate = wateringService.getStartDate(firstDayOfMonth);
        LocalDate endDate = wateringService.getEndDate(firstDayOfMonth);

        List<Watering> result = queryFactory
                .selectFrom(watering)
                .leftJoin(watering.chemical, chemical)
                .join(watering.plant, plant)
                .where(plant.gardener.gardenerId.eq((long) 1)
                        .and(watering.wateringDate.after(startDate))
                        .and(watering.wateringDate.before(endDate))
                )
                .orderBy(watering.wateringDate.asc())
                .fetch();

        List<Long> origin = waterings.stream().map(w -> w.getWateringId()).collect(Collectors.toList());
        List<Long> refactored = result.stream().map(w -> w.getWateringId()).collect(Collectors.toList());

        System.out.println(origin);
        System.out.println(refactored);

        Assertions.assertThat(origin.equals(refactored));
    }
}
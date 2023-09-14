package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static com.buckwheat.garden.data.entity.QPlace.place;
import static com.buckwheat.garden.data.entity.QPlant.plant;
import static com.buckwheat.garden.data.entity.QWatering.watering;

@SpringBootTest
class PlantRepositoryTest {
    @Autowired
    PlantRepository plantRepository;

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    @DisplayName("물주기를 기다리는 식물들")
    void findWaitingForWateringList() {
        // 파라미터
        long gardenerId = (long) 1;

        // 원본 메소드
        List<Plant> waitings = plantRepository.findWaitingForWateringList(gardenerId);

        // 리팩토링 시작
        List<Plant> waitingsTest = queryFactory
                .selectFrom(plant)
                .join(plant.place, place)
                .leftJoin(plant.waterings, watering)
                .where(plant.gardener.gardenerId.eq(gardenerId)
                        .and(watering.plant.plantId.isNull()))
                .fetch();

        // 비교하기위해 가공
        List<Long> originList = waitings.stream().map(p -> p.getPlantId()).collect(Collectors.toList());
        List<Long> refactoredList = waitingsTest.stream().map(p -> p.getPlantId()).collect(Collectors.toList());

        // PK만 찍어보기
        System.out.println(originList);
        System.out.println(refactoredList);

        Assertions.assertThat(originList.equals(refactoredList));
    }
}
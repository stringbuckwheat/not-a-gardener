package com.buckwheat.garden.data.entity;

import com.buckwheat.garden.data.dto.PlantDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "plant")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    private Long plantId;

    @NotNull
    private String name;

    private String species; // 식물 종은 null 허용

    @NotNull
    private int recentWateringPeriod; // 평균 관수 주기

    private int earlyWateringPeriod;

    @NotNull
    private String medium;

    private LocalDate birthday;

    // TODO 얘 뭐지
    private LocalDate conditionDate;

    // 물주기를 미룬 날짜
    private LocalDate postponeDate;

    @NotNull
    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="place_id")
    private Place place;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="plant")
    @OrderBy("watering_date desc")
    private List<Watering> waterings = new ArrayList<>();

    public Plant update(PlantDto.Request plantRequest, Place place){
        this.name = plantRequest.getName();
        this.medium = plantRequest.getMedium();
        this.species = plantRequest.getSpecies();
        this.recentWateringPeriod = plantRequest.getRecentWateringPeriod();
        this.birthday = plantRequest.getBirthday();
        this.place = place;

        return this;
    }

    public Plant updatePlace(Place place){
        this.place = place;
        return this;
    }

    public Plant updateAverageWateringPeriod(int averageWateringPeriod){
        this.recentWateringPeriod = averageWateringPeriod;
        return this;
    }

    public Plant updateConditionDate(){
        this.conditionDate = LocalDate.now();
        return this;
    }

    public Plant updatePostponeDate(){
        this.postponeDate = LocalDate.now();
        return this;
    }
}

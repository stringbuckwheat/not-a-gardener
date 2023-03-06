package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(name="plant_no")
    private int plantNo;

    @NotNull
    private String plantName;

    private String plantSpecies; // 식물 종은 null 허용

    @NotNull
    private int averageWateringPeriod; // 평균 관수 주기

    private int earlyWateringPeriod;

    private int fertilizingPeriod;

    private String medium;

    @NotNull
    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="place_no")
    private Place place;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="plant")
    @OrderBy("watering_date desc")
    private List<Watering> wateringList = new ArrayList<>();

    public Plant update(String plantName, String plantSpecies, String medium){
        this.plantName = plantName;
        this.plantSpecies = plantSpecies;
        this.medium = medium;

        return this;
    }

    public Plant updatePlace(Place place){
        this.place = place;
        return this;
    }
}

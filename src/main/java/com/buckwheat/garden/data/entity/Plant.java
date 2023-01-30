package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "plant")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name="plant_no")
    private int no;

    @NotNull
    private String plantName;

    private String plantSpecies; // 식물 종은 null 허용

    @NotNull
    private int averageWateringPeriod; // 평균 관수 주기

    // private int wateringStatusCode; // 물주기 상태 코드 저장

    @NotNull
    private LocalDateTime createDate;

    @NotNull
    private String username;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="plant")
    @OrderBy("watering_date desc")
    private List<Watering> wateringList = new ArrayList<>();

    public Plant update(String plantName, String plantSpecies, int averageWateringPeriod){
        this.plantName = plantName;
        this.plantSpecies = plantSpecies;
        this.averageWateringPeriod = averageWateringPeriod;

        return this;
    }
}

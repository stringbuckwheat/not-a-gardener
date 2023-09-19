package com.buckwheat.garden.data.entity;

import com.buckwheat.garden.data.dto.plant.PlantRequest;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@ToString(of = {"plantId", "name"})
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

    // not dry
    private LocalDate conditionDate;

    // 물주기를 미룬 날짜
    private LocalDate postponeDate;

    @NotNull
    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="gardener_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Gardener gardener;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="place_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="plant", cascade = CascadeType.MERGE)
    @OrderBy("watering_date desc")
    private List<Watering> waterings = new ArrayList<>();

    public Plant update(PlantRequest plantRequest, Place place){
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

    public Plant initConditionDateAndPostponeDate(){
        this.conditionDate = null;
        this.postponeDate = null;

        return this;
    }
}

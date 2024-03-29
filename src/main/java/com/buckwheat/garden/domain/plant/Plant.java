package com.buckwheat.garden.domain.plant;

import com.buckwheat.garden.domain.watering.Watering;
import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.place.Place;
import com.buckwheat.garden.domain.plant.dto.plant.PlantRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@ToString(of = {"plantId", "name", "recentWateringPeriod"})
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long plantId;

    @NotNull
    private String name;

    private String species; // 식물 종은 null 허용

    private int recentWateringPeriod; // 최근 관수 주기

    private int earlyWateringPeriod;

    private String medium;

    private LocalDate birthday;

    // not dry
    private LocalDate conditionDate;

    // 물주기를 미룬 날짜
    private LocalDate postponeDate;

    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "plant", cascade = CascadeType.MERGE)
    @OrderBy("watering_date desc")
    private List<Watering> waterings = new ArrayList<>();

    public void update(PlantRequest plantRequest, Place place) {
        this.name = plantRequest.getName();
        this.medium = plantRequest.getMedium();
        this.species = plantRequest.getSpecies();
        this.recentWateringPeriod = plantRequest.getRecentWateringPeriod();
        this.birthday = plantRequest.getBirthday();
        this.place = place;
    }

    public void updatePlace(Place place) {
        this.place = place;
    }

    public void updateRecentWateringPeriod(int averageWateringPeriod) {
        this.recentWateringPeriod = averageWateringPeriod;
    }

    public void updateConditionDate() {
        this.conditionDate = LocalDate.now();
    }

    public void updatePostponeDate() {
        this.postponeDate = LocalDate.now();
    }

    public void initEarlyWateringPeriod(int period) {
        this.earlyWateringPeriod = period;
    }

    public void initConditionDateAndPostponeDate() {
        this.conditionDate = null;
        this.postponeDate = null;
    }
}

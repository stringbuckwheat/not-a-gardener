package xyz.notagardener.plant;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.place.Place;
import xyz.notagardener.plant.plant.dto.PlantRequest;
import xyz.notagardener.status.model.Status;
import xyz.notagardener.watering.Watering;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "plant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "plant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Status status;

    @Builder
    public Plant(Long plantId, String name, String species, int recentWateringPeriod, int earlyWateringPeriod, String medium, LocalDate birthday, LocalDate conditionDate, LocalDate postponeDate, LocalDateTime createDate, Gardener gardener, Place place, List<Watering> waterings, Status status) {
        this.plantId = plantId;
        this.name = name;
        this.species = species;
        this.recentWateringPeriod = recentWateringPeriod;
        this.earlyWateringPeriod = earlyWateringPeriod;
        this.medium = medium;
        this.birthday = birthday;
        this.conditionDate = conditionDate;
        this.postponeDate = postponeDate;
        this.createDate = createDate;
        this.gardener = gardener;
        this.place = place;
        this.waterings = waterings;
        this.status = status;
    }

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

    public void updateRecentWateringPeriod(int recentWateringPeriod) {
        this.recentWateringPeriod = recentWateringPeriod;
    }

    public void updateConditionDate(LocalDate date) {
        this.conditionDate = date;
    }

    public void updatePostponeDate(LocalDate date) {
        this.postponeDate = date;
    }

    public void updateEarlyWateringPeriod(int period) {
        this.earlyWateringPeriod = period;
    }
}

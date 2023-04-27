package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "watering")
@Builder
@AllArgsConstructor // Builder 쓰려면 있어야 함
@NoArgsConstructor // Entity는 기본 생성자를 가지고 있어야 한다
public class Watering {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    private Long wateringId;

    @NotNull
    private LocalDate date; // 물 준 날짜

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="plant_id")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chemical_id")
    private Chemical chemical;

    public Watering update(LocalDate date, Plant plant, Chemical chemical){
        this.date = date;
        this.plant = plant;
        this.chemical = chemical;

        return this;
    }
}
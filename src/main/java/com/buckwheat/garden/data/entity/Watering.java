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
    private int wateringNo;

    @NotNull
    private LocalDate wateringDate; // 물 준 날짜

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="plant_no")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chemical_no")
    private Chemical chemical;

    public Watering update(LocalDate wateringDate, Plant plant, Chemical Chemical){
        this.wateringDate = wateringDate;
        this.plant = plant;
        this.chemical = Chemical;

        return this;
    }
}
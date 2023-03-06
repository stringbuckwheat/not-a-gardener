package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "pesticide_date")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PesticideDate {
    /* 농약, 살균, 살충제 관주 및 도포 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pesticideDateNo;

    @NotNull
    private LocalDate pesticideDate;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plant_no")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pesticide_no")
    private Pesticide pesticide;

    public PesticideDate update(LocalDate pesticideDate, Plant plant, Pesticide pesticide){
        this.pesticideDate = pesticideDate;
        this.plant = plant;
        this.pesticide = pesticide;

        return this;
    }

}

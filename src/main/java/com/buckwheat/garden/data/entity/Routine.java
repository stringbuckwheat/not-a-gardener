package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "routine")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int routineNo;

    @NotNull
    private String routineContent;

    @NotNull
    private int routineCycle;

    private LocalDate lastCompleteDate;

    @NotNull
    private LocalDate createDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="plant_no")
    private Plant plant;

    public Routine update(String routineContent, int routineCycle, Plant plant){
        this.routineContent = routineContent;
        this.routineCycle = routineCycle;
        this.plant = plant;

        return this;
    }

    public Routine complete(LocalDate lastCompleteDate){
        this.lastCompleteDate = lastCompleteDate;
        return this;
    }
}

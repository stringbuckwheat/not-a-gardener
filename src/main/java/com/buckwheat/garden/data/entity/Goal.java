package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    /* 올해 식물 키우기 목표 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int goalNo;

    @NotNull
    private String goalContent;

    private String complete;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plant_no")
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    public Goal update(String goalContent, Plant plant){
        this.goalContent = goalContent;
        this.plant = plant;

        return this;
    }

    public Goal completeGoal(String complete){
        this.complete = complete;

        return this;
    }
}

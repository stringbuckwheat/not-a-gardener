package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    /* 식물 키우기 목표 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long goalId;

    // 목표 내용
    @NotNull
    private String content;

    // 달성 여부
    private String complete;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plant_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gardener_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Gardener gardener;

    public Goal update(String content, Plant plant){
        this.content = content;
        this.plant = plant;

        return this;
    }

    public Goal completeGoal(String complete){
        this.complete = complete;

        return this;
    }
}

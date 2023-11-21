package com.buckwheat.garden.domain.goal;

import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.gardener.Gardener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    // 목표 내용
    @NotNull
    private String content;

    // 달성 여부
    private String complete;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    public void update(String content, Plant plant) {
        this.content = content;
        this.plant = plant;
    }

    public void completeGoal(String complete) {
        this.complete = complete;
    }
}

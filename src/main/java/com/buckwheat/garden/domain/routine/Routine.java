package com.buckwheat.garden.domain.routine;

import com.buckwheat.garden.domain.gardener.Gardener;
import com.buckwheat.garden.domain.plant.Plant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "routine")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Routine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routineId;

    // 루틴 내용
    @NotNull
    private String content;

    // 반복 주기
    @NotNull
    private int cycle;

    // 가장 최근 완료한 날짜
    private LocalDate lastCompleteDate;

    @NotNull
    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    public void update(String content, int cycle, Plant plant) {
        this.content = content;
        this.cycle = cycle;
        this.plant = plant;
    }

    public void complete(LocalDate lastCompleteDate) {
        this.lastCompleteDate = lastCompleteDate;
    }
}

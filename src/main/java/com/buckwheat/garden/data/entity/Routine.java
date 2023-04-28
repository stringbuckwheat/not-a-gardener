package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="gardener_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Gardener gardener;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="plant_id")
    private Plant plant;

    public Routine update(String content, int cycle, Plant plant){
        this.content = content;
        this.cycle = cycle;
        this.plant = plant;

        return this;
    }

    public Routine complete(LocalDate lastCompleteDate){
        this.lastCompleteDate = lastCompleteDate;
        return this;
    }
}

package xyz.notagardener.routine;

import lombok.*;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.plant.Plant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "routine")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Routine(Long routineId, String content, int cycle, LocalDate lastCompleteDate, LocalDate createDate, Gardener gardener, Plant plant) {
        this.routineId = routineId;
        this.content = content;
        this.cycle = cycle;
        this.lastCompleteDate = lastCompleteDate;
        this.createDate = createDate;
        this.gardener = gardener;
        this.plant = plant;
    }

    public void update(String content, int cycle, Plant plant) {
        this.content = content;
        this.cycle = cycle;
        this.plant = plant;
    }

    public void complete(LocalDate lastCompleteDate) {
        this.lastCompleteDate = lastCompleteDate;
    }
}

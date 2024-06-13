package xyz.notagardener.goal;

import lombok.*;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.gardener.Gardener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Table(name = "goal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Goal(Long goalId, String content, String complete, Plant plant, Gardener gardener) {
        this.goalId = goalId;
        this.content = content;
        this.complete = complete;
        this.plant = plant;
        this.gardener = gardener;
    }

    public void update(String content, Plant plant) {
        this.content = content;
        this.plant = plant;
    }

    public void completeGoal(String complete) {
        this.complete = complete;
    }
}

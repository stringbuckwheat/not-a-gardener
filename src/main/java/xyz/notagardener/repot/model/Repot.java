package xyz.notagardener.repot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.model.Plant;

import java.time.LocalDate;

@Entity
@Table(name = "repot", uniqueConstraints = {@UniqueConstraint(columnNames = {"repot_date", "plant_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Repot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repotId;

    @NotNull
    @Column(name = "repot_date")
    private LocalDate repotDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private YesOrNoType initPeriod;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    @Builder
    public Repot(Long repotId, LocalDate repotDate, YesOrNoType initPeriod, Plant plant) {
        this.repotId = repotId;
        this.repotDate = repotDate;
        this.initPeriod = initPeriod;
        this.plant = plant;
    }

    public Repot(LocalDate repotDate, Plant plant, YesOrNoType initPeriod) {
        this.repotDate = repotDate;
        this.plant = plant;
        this.initPeriod = initPeriod;
    }

    public void update(LocalDate repotDate) {
        this.repotDate = repotDate;
    }
}
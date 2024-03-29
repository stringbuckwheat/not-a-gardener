package com.buckwheat.garden.domain.watering;

import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.plant.Plant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "watering")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of={"wateringId", "wateringDate", "chemical.name"})
public class Watering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long wateringId;

    @NotNull
    private LocalDate wateringDate; // 물 준 날짜

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chemical_id")
    private Chemical chemical;

    public Watering update(LocalDate wateringDate, Plant plant, Chemical chemical) {
        this.wateringDate = wateringDate;
        this.plant = plant;
        this.chemical = chemical;

        return this;
    }
}
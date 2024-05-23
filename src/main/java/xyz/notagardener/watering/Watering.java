package xyz.notagardener.watering;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.chemical.Chemical;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;

@Entity
@Table(name = "watering", uniqueConstraints = {@UniqueConstraint(columnNames = {"plant_id", "watering_date"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(of={"wateringId", "wateringDate", "chemical.name"})
public class Watering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wateringId;

    @NotNull
    @Column(name = "watering_date")
    private LocalDate wateringDate; // 물 준 날짜

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
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
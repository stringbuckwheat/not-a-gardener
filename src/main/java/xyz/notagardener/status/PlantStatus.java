package xyz.notagardener.status;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;

@Entity
@Table(name = "plant_status",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"plant_id", "recorded_date"})}
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PlantStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantStatusId;

    @NotBlank
    private String status;

    @NotNull
    @Column(name = "recorded_date")
    private LocalDate recordedDate;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    public void update(String status, LocalDate recordedDate, Plant plant) {
        this.status = status;
        this.recordedDate = recordedDate;
        this.plant = plant;
    }
}
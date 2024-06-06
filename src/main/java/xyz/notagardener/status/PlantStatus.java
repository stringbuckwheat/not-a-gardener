package xyz.notagardener.status;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.status.dto.PlantStatusType;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plant_status")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PlantStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long plantStatusId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PlantStatusType status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private YesOrNoType active;

    @NotNull
    @Column(name = "recorded_date")
    private LocalDate recordedDate;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @CreatedDate
    private LocalDateTime createDate;

    public void update(PlantStatusType status, LocalDate recordedDate, Plant plant) {
        this.status = status;
        this.recordedDate = recordedDate;
        this.plant = plant;
    }
}
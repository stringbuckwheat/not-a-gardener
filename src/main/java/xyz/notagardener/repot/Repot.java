package xyz.notagardener.repot;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import xyz.notagardener.plant.Plant;

import java.time.LocalDate;

@Entity
@Table(name = "repot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Repot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repotId;

    @NotNull
    private LocalDate repotDate;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    public Repot(LocalDate repotDate, Plant plant) {
        this.repotDate = repotDate;
        this.plant = plant;
    }

    public void update(LocalDate repotDate) {
        this.repotDate = repotDate;
    }
}
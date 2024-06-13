package xyz.notagardener.status.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.Plant;

import java.time.LocalDateTime;

@Entity
@Table(name = "status")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;

    @Enumerated(EnumType.STRING)
    private YesOrNoType attention;

    @Enumerated(EnumType.STRING)
    private YesOrNoType heavyDrinker;

    @OneToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @CreatedDate
    private LocalDateTime createDate;

    @Builder
    public Status(YesOrNoType attention, YesOrNoType heavyDrinker, Plant plant) {
        this.attention = attention;
        this.heavyDrinker = heavyDrinker;
        this.plant = plant;
    }

    public void update(StatusType statusType, YesOrNoType active) {
        if (StatusType.HEAVY_DRINKER.equals(statusType)) {
            this.heavyDrinker = active;
        } else if (StatusType.ATTENTION_PLEASE.equals(statusType)) {
            this.attention = active;
        }
    }

    public void update(Plant plant) {
        this.plant = plant;
    }

    public void update(StatusType statusType, YesOrNoType active, Plant plant) {
        update(statusType, active);
        update(plant);
    }

    public void init() {
        this.attention = YesOrNoType.N;
        this.heavyDrinker = YesOrNoType.N;
    }
}

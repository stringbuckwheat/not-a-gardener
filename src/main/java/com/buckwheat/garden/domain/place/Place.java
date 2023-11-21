package com.buckwheat.garden.domain.place;

import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.gardener.Gardener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    // 장소 이름
    @NotNull
    private String name;

    // 실내, 야외, 베란다...
    @NotNull
    private String option;

    // 식물등 사용 여부
    @NotNull
    private String artificialLight;

    @NotNull
    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gardener_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gardener gardener;

    // 양방향 매핑
    @OneToMany(mappedBy = "place")
    @OrderBy("create_date DESC")
    private List<Plant> plants = new ArrayList<>();

    public void update(String name, String option, String artificialLight) {
        this.name = name;
        this.option = option;
        this.artificialLight = artificialLight;
    }
}

package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    /* 식물을 놓은 장소 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    // 양방향 매핑
    @OneToMany(mappedBy = "place")
    @OrderBy("create_date DESC")
    private List<Plant> plants = new ArrayList<>();

    public Place update(String name, String option, String artificialLight){
        this.name = name;
        this.option = option;
        this.artificialLight = artificialLight;

        return this;
    }
}

package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    private int placeNo;

    @NotNull
    private String placeName;

    private String artificialLight;

    private String option;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    // 양방향 매핑
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "place")
    @OrderBy("create_date desc")
    private List<Plant> plantList = new ArrayList<>();
}

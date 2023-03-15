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
    private int placeNo;

    @NotNull
    private String placeName;

    private String artificialLight;

    @NotNull
    private String option;

    @NotNull
    private LocalDateTime createDate;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    // 양방향 매핑
    @OneToMany(mappedBy = "place")
    @OrderBy("create_date DESC")
    private List<Plant> plantList = new ArrayList<>();

    public Place update(String placeName, String option, String artificialLight){
        this.placeName = placeName;
        this.option = option;
        this.artificialLight = artificialLight;

        return this;
    }
}

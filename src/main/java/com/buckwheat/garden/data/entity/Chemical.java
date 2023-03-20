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
@Table(name = "Chemical")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chemical {
    /* 비료 정보 기록 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chemical_no")
    private int chemicalNo;

    @NotNull
    private String chemicalName;

    @NotNull
    private String chemicalType;

    @NotNull
    private int chemicalPeriod;

    // 외래키가 있는 곳이 연관관계의 주인
    // 양방향 매핑 시 반대편에 mappedBy
    // 그러나 Member는 Chemical를 몰라도 상관없으므로 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chemical")
    @OrderBy("watering_date desc")
    private List<Watering> wateringList = new ArrayList<>();
}

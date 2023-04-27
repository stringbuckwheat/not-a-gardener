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
    private Long chemicalId;

    // 약품 이름
    @NotNull
    private String name;

    // 약품 종류: ex) 기본 NPK 비료, 미량 원소 비료, 살충제...
    @NotNull
    private String type;

    // 시비/살포 주기
    @NotNull
    private int period;

    // 외래키가 있는 곳이 연관관계의 주인
    // 양방향 매핑 시 반대편에 mappedBy
    // 그러나 Member는 Chemical를 몰라도 상관없으므로 단방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chemical")
    @OrderBy("watering_date desc")
    private List<Watering> waterings = new ArrayList<>();
}

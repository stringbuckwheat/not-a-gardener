package com.buckwheat.garden.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fertilizer")
@Getter
public class Fertilizer {
    /* 비료 정보 기록 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int fertilizerNo;

    @NotNull
    private String fertilizerName;

    @NotNull
    private int fertilizingPeriod;

    // 외래키가 있는 곳이 연관관계의 주인
    // 양방향 매핑 시 반대편에 mappedBy
    // 그러나 Member는 Fertilizer를 몰라도 상관없으므로 단방향 매핑
    @ManyToOne
    @JoinColumn(name="username")
    private Member member;
}

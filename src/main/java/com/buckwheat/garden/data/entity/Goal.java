package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "goal")
public class Goal {
    /* 올해 식물 키우기 목표 */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int goalNo;

    @NotNull
    private String goalTitle;

    // mysql 컬럼 타입 text를 사용하기 위한 어노테이션
    @Column(columnDefinition = "TEXT")
    private String goalContent;

    private int complete;

    // FK
    @ManyToOne
    @JoinColumn(name="plant_no")
    private Plant plant;

    @ManyToOne
    @JoinColumn(name="username")
    private Member member;
}

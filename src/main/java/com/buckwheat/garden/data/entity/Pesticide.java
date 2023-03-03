package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pesticide")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pesticide {
    /* 농약 정보 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pesticideNo;

    @NotNull
    private String pesticideName;

    @NotNull
    private int pesticidePeriod;

    private String pesticideType;

    // FK
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_no")
    private Member member;

    public Pesticide update(String pesticideName, int pesticidePeriod){
        this.pesticideName = pesticideName;
        this.pesticidePeriod = pesticidePeriod;

        return this;
    }
}

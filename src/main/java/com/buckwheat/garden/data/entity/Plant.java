package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "plant")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Plant {
    @Id
    private int plantNo;

    @NotNull
    private String plantName;

    private String plantSpecies; // 식물 종은 null 허용

    @NotNull
    private int averageWateringPeriod; // 평균 관수 주기

    @NotNull
    private LocalDateTime createDate;

    // user의 id를 FK로 지정하기 위해
    // @ManyToOne, @JoinColumn(name="id") 등을 썼는데,
    // 그러니까 너무 많은 정보가 포함됨... 해결할 줄 몰라서 일단 이렇게
    @NotNull
    private String username;
}

package com.buckwheat.garden.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "watering")
@Builder
@AllArgsConstructor // Builder 쓰려면 있어야 함
@NoArgsConstructor // Entity는 기본 생성자를 가지고 있어야 한다
public class Watering {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    private int wateringNo;

    @NotNull
    private LocalDate wateringDate; // 물 준 날짜

    private String fertilized;

    private int plantNo;
}

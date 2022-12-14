package com.buckwheat.garden.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "watering")
public class Watering {
    @Id
    private int wateringNo;

    @NotNull
    private LocalDate wateringDate; // 물 준 날짜

    @Enumerated(EnumType.STRING)
    @Column(length = 2)
    private Fertilizer fertilized;
}

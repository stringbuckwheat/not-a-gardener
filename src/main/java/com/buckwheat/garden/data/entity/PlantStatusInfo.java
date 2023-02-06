package com.buckwheat.garden.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "plant_status_info")
public class PlantStatusInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int statusNo;

    @NotNull
    private String statusName;
}

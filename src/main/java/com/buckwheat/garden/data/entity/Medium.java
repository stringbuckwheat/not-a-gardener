package com.buckwheat.garden.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "medium")
public class Medium {
    /* 식물 식재 용토 */
    @Id
    private int mediumNo;

    private String mediumName;
}

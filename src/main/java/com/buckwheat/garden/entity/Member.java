package com.buckwheat.garden.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="member")
@Data
public class Member {
    @Id // primary key
    private String id;

    private String pw;

    private String name;

    @Column(name="create_date")
    private LocalDateTime createDate;
}
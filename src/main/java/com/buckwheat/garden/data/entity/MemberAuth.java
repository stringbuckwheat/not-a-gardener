package com.buckwheat.garden.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "member")
public class MemberAuth {
    @Id
    private String id;
    private String pw;
    private String name;
}

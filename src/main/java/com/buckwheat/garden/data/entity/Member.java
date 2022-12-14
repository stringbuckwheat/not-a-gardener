package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {
    @Id
    private String id;

    @NotNull
    private String email;

    @NotNull
    private String pw;

    @NotNull
    private String name;

    @Column(name="create_date")
    private LocalDateTime createDate;
}

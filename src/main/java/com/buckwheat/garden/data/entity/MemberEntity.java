package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberEntity {
    @Id
    private String id;

    private String email;

    private String pw;

    private String name;

    @Column(name="create_date")
    private LocalDateTime createDate;

    // DTO -> entity


}

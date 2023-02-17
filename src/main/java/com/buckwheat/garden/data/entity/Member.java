package com.buckwheat.garden.data.entity;

import lombok.*;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int memberNo;

    private String username;

    private String pw;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime createDate;

    private String provider;

    public Member changePassword(String encryptPassword){
        this.pw = encryptPassword;
        return this;
    }
}

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
    private Long memberId;

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime createDate;

    private String provider;

    public Member changePassword(String encryptPassword){
        this.password = encryptPassword;
        return this;
    }

    public Member updateEmailAndName(String email, String name){
        this.email = email;
        this.name = name;

        return this;
    }
}

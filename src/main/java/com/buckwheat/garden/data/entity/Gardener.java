package com.buckwheat.garden.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "gardener")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Gardener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gardenerId;

    @NotNull
    private String username;

    private String password;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime createDate;
    private LocalDateTime recentLogin;

    private String provider;

    public void changePassword(String encryptPassword) {
        this.password = encryptPassword;
    }

    public void updateEmailAndName(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public void updateRecentLogin() {
        this.recentLogin = LocalDateTime.now();
    }
}

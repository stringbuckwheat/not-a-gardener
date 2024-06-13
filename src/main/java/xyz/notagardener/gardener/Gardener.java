package xyz.notagardener.gardener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "gardener")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Gardener(Long gardenerId, String username, String password, String email, String name, LocalDateTime createDate, LocalDateTime recentLogin, String provider) {
        this.gardenerId = gardenerId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.createDate = createDate;
        this.recentLogin = recentLogin;
        this.provider = provider;
    }

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

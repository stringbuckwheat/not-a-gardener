package xyz.notagardener.gardener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "gardener")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gardener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gardenerId;

    @NotNull
    @Setter
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

    @Column(nullable = true)
    private String profileImageUrl;

    @OneToMany(mappedBy = "gardener")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(mappedBy = "gardener")
    private List<Plant> plants = new ArrayList<>();

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

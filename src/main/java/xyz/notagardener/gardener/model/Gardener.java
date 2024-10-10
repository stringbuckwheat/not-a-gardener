package xyz.notagardener.gardener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import xyz.notagardener.follow.model.Follow;
import xyz.notagardener.plant.model.Plant;
import xyz.notagardener.post.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gardener")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
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

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime recentLogin;

    private String provider;

    @Column(nullable = true)
    private String profileImageUrl;

    private String biography;

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

    public void updateSocialInfo(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }
}

package xyz.notagardener.gardener.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import xyz.notagardener.gardener.model.Gardener;

@Getter
public class SocialGardenerInfo {
    private Long gardenerId;
    private String profileImageUrl;
    private String username;
    private String name;

    @QueryProjection
    public SocialGardenerInfo(Gardener gardener) {
        this.gardenerId = gardener.getGardenerId();
        this.profileImageUrl = gardener.getProfileImageUrl();
        this.username = gardener.getUsername();
        this.name = gardener.getName();
    }
}

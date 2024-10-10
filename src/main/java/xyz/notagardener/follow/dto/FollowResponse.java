package xyz.notagardener.follow.dto;

import lombok.Getter;
import xyz.notagardener.gardener.model.Gardener;

@Getter
public class FollowResponse {
    private Long id;
    private String username;
    private String name;

    public FollowResponse(Gardener gardener) {
        this.id = gardener.getGardenerId();
        this.username = gardener.getUsername();
        this.name = gardener.getName();
    }
}

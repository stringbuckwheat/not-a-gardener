package xyz.notagardener.gardener.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Username {
    private String username;

    @QueryProjection
    public Username(String username) {
        this.username = username;
    }
}

package com.buckwheat.garden.data.dto.gardener;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 관리 페이지
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class GardenerDetail {
    private Long id;
    private String username;
    private String email;
    private String name;
    private LocalDateTime createDate;
    private String provider;

    @QueryProjection
    public GardenerDetail(Long id, String username, String email, String name, LocalDateTime createDate, String provider) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.createDate = createDate;
        this.provider = provider;
    }
}

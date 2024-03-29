package com.buckwheat.garden.domain.gardener.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "회원 id", example = "1")
    private Long id;

    @Schema(description = "아이디", example = "perfectgardener")
    private String username;

    @Schema(description = "이메일", example = "perfectgardener@gardener.com")
    private String email;

    @Schema(description = "이름", example = "식집사")
    private String name;

    @Schema(description = "가입일", example = "2024-02-01")
    private LocalDateTime createDate;

    @Schema(description = "소셜로그인 서비스")
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

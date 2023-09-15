package com.buckwheat.garden.data.dto.gardener;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 회원 관리 페이지
 */
@AllArgsConstructor
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

    public static GardenerDetail from(Gardener gardener) {
        return GardenerDetail.builder()
                .id(gardener.getGardenerId())
                .username(gardener.getUsername())
                .email(gardener.getEmail())
                .name(gardener.getName())
                .createDate(gardener.getCreateDate())
                .provider(gardener.getProvider())
                .build();
    }
}

package com.buckwheat.garden.data.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor // request body에 필요
@Builder
@AllArgsConstructor // builder 쓰려면 필요
@ToString // 확인용
public class MemberDetailDto {
    private int memberNo;
    private String username;
    private String email;
    private String name;
    private LocalDateTime createDate;
    private String provider;
}

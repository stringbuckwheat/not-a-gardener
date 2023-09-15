package com.buckwheat.garden.data.dto.gardener;

import com.buckwheat.garden.data.projection.Username;
import lombok.*;

import java.util.List;

/**
 * 아이디/비밀번호 찾기 응답
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Forgot {
    private String identificationCode;
    private String email;
    List<Username> usernames;
}

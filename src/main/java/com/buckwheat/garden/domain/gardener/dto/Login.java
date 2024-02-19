package com.buckwheat.garden.domain.gardener.dto;

import com.buckwheat.garden.domain.gardener.Gardener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

/**
 * 로그인 요청
 */
@Getter
@ToString
public class Login {
    @Schema(description = "아이디", example = "futurefarmer")
    private String username;
    @Schema(description = "비밀번호", example = "nowgardener")
    private String password;

    public void encryptPassword(String bCryptpassword) {
        this.password = bCryptpassword;
    }

    public Gardener toEntity() {
        return Gardener.builder().username(username).password(password).build();
    }
}

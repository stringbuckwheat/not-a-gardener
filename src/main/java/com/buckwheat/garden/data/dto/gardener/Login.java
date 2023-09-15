package com.buckwheat.garden.data.dto.gardener;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.Getter;
import lombok.ToString;

/**
 * 로그인 요청
 */
@Getter
@ToString
public class Login {
    private String username;
    private String password;

    public void encryptPassword(String bCryptpassword) {
        this.password = bCryptpassword;
    }

    public Gardener toEntity() {
        return Gardener.builder().username(username).password(password).build();
    }
}

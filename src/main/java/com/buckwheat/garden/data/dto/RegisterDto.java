package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterDto {
    private String id;
    private String email;
    private String pw;
    private String name;

    /* 암호화된 password */
    public void encryptPassword(String BCryptpassword) {
        this.pw = BCryptpassword;
    }

    public Member toEntity(){
        return Member
                .builder()
                .id(id)
                .email(email)
                .pw(pw)
                .name(name)
                .createDate(LocalDateTime.now())
                .build();
    }
}

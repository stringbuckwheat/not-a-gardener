package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import lombok.Data;

@Data
public class MemberDto {
    private String username;
    private String pw;

    public void encryptPassword(String BCryptpassword) {
        this.pw = BCryptpassword;
    }

    public Member toEntity(){
        return Member
                .builder()
                .username(username)
                .pw(pw)
                .build();
    }
}

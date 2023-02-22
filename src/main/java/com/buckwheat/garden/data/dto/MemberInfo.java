package com.buckwheat.garden.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter // HttpMediaTypeNotAcceptableException
public class MemberInfo {
    private String token;
    private int memberNo;
    private String name;

    public static MemberInfo getMemberInfo(int memberNo, String name){
        return new MemberInfo(null, memberNo, name);
    }
}

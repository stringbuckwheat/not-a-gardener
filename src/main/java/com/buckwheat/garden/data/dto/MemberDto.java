package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Member;
import lombok.*;

import java.time.LocalDateTime;

public class MemberDto {
    @Getter
    @ToString
    public static class Login {
        private String username;
        private String password;

        public void encryptPassword(String BCryptpassword) {
            this.password = BCryptpassword;
        }

        public Member toEntity() {
            return Member.builder().username(username).password(password).build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter // HttpMediaTypeNotAcceptableException
    public static class Info {
        private String token;
        private Long memberId;
        private String name;
        private String provider;

        public static Info from(String jwtToken, Member member) {
            return Info.builder()
                    .token(jwtToken)
                    .memberId(member.getMemberId())
                    .name(member.getName())
                    .provider(member.getProvider())
                    .build();
        }
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @NoArgsConstructor
    @ToString
    public static class Detail {
        private Long id;
        private String username;
        private String email;
        private String name;
        private LocalDateTime createDate;
        private String provider;

        public static Detail from (Member member) {
            return MemberDto.Detail.builder()
                    .id(member.getMemberId())
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .name(member.getName())
                    .createDate(member.getCreateDate())
                    .provider(member.getProvider())
                    .build();
        }

        public static Detail updateResponseFrom(Member member){
            return MemberDto.Detail.builder()
                    .username(member.getUsername())
                    .email(member.getEmail())
                    .name(member.getName())
                    .createDate(member.getCreateDate())
                    .build();
        }
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        private String username;
        private String email;
        private String password;
        private String name;

        /* 암호화된 password builder 패턴으로 사용중*/
        public Register encryptPassword(String BCryptpassword) {
            this.password = BCryptpassword;
            return this;
        }

        public Member toEntity(){
            return Member
                    .builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .name(name)
                    .createDate(LocalDateTime.now())
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    @ToString
    public static class ForgotResponse{
        private Long id;
        private String username;
        private String provider;
    }
}

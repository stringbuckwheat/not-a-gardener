package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.RefreshToken;
import lombok.*;

import java.time.LocalDateTime;

public class GardenerDto {
    /**
     * 로그인 요청
     */
    @Getter
    @ToString
    public static class Login {
        private String username;
        private String password;

        public void encryptPassword(String BCryptpassword) {
            this.password = BCryptpassword;
        }

        public Gardener toEntity() {
            return Gardener.builder().username(username).password(password).build();
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Token {
        private String accessToken;
        private String refreshToken;
        private LocalDateTime expiredAt;

        public static Token from(AccessToken accessToken, RefreshToken refreshToken) {
            return new Token(
                    accessToken.getToken(),
                    refreshToken.getToken(),
                    accessToken.getExpiredAt()
            );
        }

        public static Token from(AccessToken accessToken){
            return new Token(accessToken.getToken(), null, accessToken.getExpiredAt());
        }
    }

    @Getter
    @ToString
    public static class Refresh {
        private Long gardenerId;
        private String refreshToken;
    }

    /**
     * 로그인 이후 토큰과 기본 정보 응답
     */
    @AllArgsConstructor
    @Getter // HttpMediaTypeNotAcceptableException
    public static class Info {
        private SimpleInfo simpleInfo;
        private Token token;

        public static Info from(AccessToken accessToken, RefreshToken refreshToken, Gardener gardener) {
            return new Info(SimpleInfo.from(gardener), Token.from(accessToken, refreshToken));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class SimpleInfo {
        private Long gardenerId;
        private String name;
        private String provider;

        public static SimpleInfo from(Gardener gardener) {
            return new SimpleInfo(gardener.getGardenerId(), gardener.getName(), gardener.getProvider());
        }
    }

    /**
     * 회원 관리 페이지
     */
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

        public static Detail from(Gardener gardener) {
            return GardenerDto.Detail.builder()
                    .id(gardener.getGardenerId())
                    .username(gardener.getUsername())
                    .email(gardener.getEmail())
                    .name(gardener.getName())
                    .createDate(gardener.getCreateDate())
                    .provider(gardener.getProvider())
                    .build();
        }

        public static Detail updateResponseFrom(Gardener gardener) {
            return GardenerDto.Detail.builder()
                    .id(gardener.getGardenerId())
                    .username(gardener.getUsername())
                    .email(gardener.getEmail())
                    .name(gardener.getName())
                    .createDate(gardener.getCreateDate())
                    .build();
        }
    }

    /**
     * 회원가입 요청
     */
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

        public Gardener toEntity() {
            return Gardener
                    .builder()
                    .username(username)
                    .email(email)
                    .password(password)
                    .name(name)
                    .createDate(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 아이디/비밀번호 찾기 응답
     */
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    @ToString
    public static class ForgotResponse {
        private Long id;
        private String username;
        private String provider;
    }
}

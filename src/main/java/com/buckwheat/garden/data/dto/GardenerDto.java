package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

import java.time.LocalDateTime;

public class GardenerDto {
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
    @Builder
    @Getter // HttpMediaTypeNotAcceptableException
    public static class Info {
        private String token;
        private Long gardenerId;
        private String name;
        private String provider;

        public static Info from(String jwtToken, Gardener gardener) {
            return Info.builder()
                    .token(jwtToken)
                    .gardenerId(gardener.getGardenerId())
                    .name(gardener.getName())
                    .provider(gardener.getProvider())
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

        public static Detail from (Gardener gardener) {
            return GardenerDto.Detail.builder()
                    .id(gardener.getGardenerId())
                    .username(gardener.getUsername())
                    .email(gardener.getEmail())
                    .name(gardener.getName())
                    .createDate(gardener.getCreateDate())
                    .provider(gardener.getProvider())
                    .build();
        }

        public static Detail updateResponseFrom(Gardener gardener){
            return GardenerDto.Detail.builder()
                    .id(gardener.getGardenerId())
                    .username(gardener.getUsername())
                    .email(gardener.getEmail())
                    .name(gardener.getName())
                    .createDate(gardener.getCreateDate())
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

        public Gardener toEntity(){
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

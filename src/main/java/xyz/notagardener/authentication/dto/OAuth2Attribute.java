package xyz.notagardener.authentication.dto;

import xyz.notagardener.gardener.model.Gardener;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

// 사용자 정보를 담을 Class
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String email;
    private String name;
    private String provider;

    public static OAuth2Attribute of(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributes);
            case "kakao":
                return ofKakao(attributes);
            case "naver":
                return ofNaver(attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .attributes(attributes)
                .provider("google")
                .build();
    }

    private static OAuth2Attribute ofKakao(
            Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .attributes(kakaoAccount)
                .provider("kakao")
                .build();
    }

    private static OAuth2Attribute ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .attributes(response)
                .provider("naver")
                .build();
    }

    public Gardener toEntity() {
        return Gardener
                .builder()
                .email(this.getEmail())
                .password(null)
                .name(this.getName())
                .provider(this.provider)
                .createDate(LocalDateTime.now())
                .build();
    }
}

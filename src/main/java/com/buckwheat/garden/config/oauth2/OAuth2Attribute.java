package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.data.entity.Gardener;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// 사용자 정보를 담을 Class
@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;
    private String picture;
    private String provider;

    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes){
        switch(provider){
            case "google":
                return ofGoogle(attributeKey, attributes);
            case "kakao":
                return ofKakao("email", attributes);
            case "naver":
                return ofNaver("id", attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .picture(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .attributeKey(attributeKey)
                .provider("google")
                .build();
    }

    private static OAuth2Attribute ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuth2Attribute.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .picture(String.valueOf(kakaoProfile.get("profile_image_url")))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .provider("kakao")
                .build();
    }

    private static OAuth2Attribute ofNaver(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuth2Attribute.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .picture(String.valueOf(response.get("profile_image")))
                .attributes(response)
                .attributeKey(attributeKey)
                .provider("naver")
                .build();
    }

    Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("picture", picture);

        return map;
    }

    public Gardener toEntity(){
        // 신규 생성에만 쓰기 때문에 gardenerId는 없음
        return Gardener
                .builder()
                .username(this.getEmail())
                .email(this.getEmail())
                .password(null)
                .name(this.getName())
                .provider(this.provider)
                .createDate(LocalDateTime.now())
                .build();
    }
}

package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
// Implementations of this interface
// are responsible for obtaining the user attributes of the End-User(Resource Owner)
// from the UserInfo Endpoint
// using the Access Token granted to the Client
// and returning an AuthenticatedPrincipal in the form of an OAuth2User.
public class CustomOAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final LoginRepository loginRepository;
    private final HttpSession httpSession;

    // 하나의 메소드를 오버라이딩 해야함
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Returns an OAuth2User after obtaining the user attributes of the End-User from the UserInfo Endpoint.

        // OAuth2UserService, OAuth2User, DefaultOAuth2UserService()는 모두 security.oauth2 안에 있는 클래스
        // 성공정보를 바탕으로 DefaultOAuth2UserService 객체를 만든다
        // OAuth2UserService<R extends OAuth2UserRequest, U extends OAuth2User>
        // DefaultOAuth2User 서비스를 통해 User 정보를 가져와야 하기 때문에 대리자 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // 생성된 서비스 객체로부터 User를 받는다
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.debug("oAuth2User: {}", oAuth2User.getAttributes());

        // User 정보 받기
        // 구글 로그인인지, 네이버 로그인인지 등등을 알려줌
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth를 지원하는 소셜 서비스들간의 약속
        // 어떤 소셜서비스든 그 서비스에서 각 계정마다의 유니크한 id값을 전달해주겠다
        // 구글은 sub, 네이버는 id 필드가 유니크 필드
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.debug("registrationId: {}", registrationId);
        log.debug("userNameAttributeName: " + userNameAttributeName);

        // OAuth2UserService를 통해 가져온 데이터를 담을 클래스
        // attribute: {name=최서경, id=sub, key=sub, email=stringbuckwheat@gmail.com, picture=https://lh3.googleusercontent.com/a/AEdFTp6LDv3ehxMKkXSnPuHZDM9vQrdwJtEdJfFclfIn=s96-c}
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        log.debug("oAuth2Attribute: {}", oAuth2Attribute);
        log.debug("memberAttribute: {}", memberAttribute.toString());

        // save or update
        loginRepository.save(oAuth2Attribute.toEntity(memberAttribute));

        // 로그인한 유저를 리턴
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2Attribute.getAttributes(),
                oAuth2Attribute.getAttributeKey());
    }
}

package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
// Implementations of this interface
// are responsible for obtaining the user attributes of the End-User(Resource Owner)
// from the UserInfo Endpoint
// using the Access Token granted to the Client
// and returning an AuthenticatedPrincipal in the form of an OAuth2User.
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;

    // 하나의 메소드를 오버라이딩 해야함
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Returns an OAuth2User after obtaining the user attributes of the End-User from the UserInfo Endpoint.

        // OAuth2UserService, OAuth2User, DefaultOAuth2UserService()는 모두 security.oauth2 안에 있는 클래스
        // 성공정보를 바탕으로 DefaultOAuth2UserService 객체를 만든다
        // OAuth2UserService<R extends OAuth2UserRequest, U extends OAuth2User>
        // DefaultOAuth2User 서비스를 통해 User 정보를 가져와야 하기 때문에 대리자 생성
        // 생성된 서비스 객체로부터 User를 받는다
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
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
        // attribute: {name, id, key, email, picture}
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        // 기존 회원이면 update, 신규 회원이면 save
        Member member = saveOrUpdate(oAuth2Attribute);

        Map<String, Object> attributes = oAuth2Attribute.toMap();
        attributes.put("memberNo", member.getMemberNo());

        // UserPrincipal: Authentication에 담을 OAuth2User와 (일반 로그인 용) UserDetails를 implements한 커스텀 클래스
        return UserPrincipal.create(member, oAuth2Attribute.getAttributes());
    }

    public Member saveOrUpdate(OAuth2Attribute oAuth2Attribute){
        Member member = memberRepository.findByUsernameAndProvider(oAuth2Attribute.getEmail(), oAuth2Attribute.getProvider())
                .orElse(oAuth2Attribute.toEntity());

        return memberRepository.save(member);
    }
}

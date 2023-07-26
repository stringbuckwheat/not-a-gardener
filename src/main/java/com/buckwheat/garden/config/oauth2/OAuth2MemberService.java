package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.entity.Gardener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * access token을 사용하여 OAuth2 서버에서 유저 정보를 가져온다.
 * 데이터베이스에 Gardener를 저장/수정한다.
 * 가져온 유저 정보로 UserPrincipal을 만들어 반환한다.
 * UserPrincipal: UserDetails, OAuth2User를 implements한 커스텀 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final GardenerDao gardenerDao;

    /**
     * OAuth2 로그인 성공 정보를 바탕으로 UserPrincipal을 만들어 반환한다
     * @param userRequest 로그인한 유저 리퀘스트
     * @return Authenticaion 객체에 담을 UserPrincipal(Custom)
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 성공정보를 바탕으로 DefaultOAuth2UserService 객체를 만든 뒤 User를 받는다
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // 구글 로그인인지, 네이버 로그인인지
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth를 지원하는 소셜 서비스들간의 약속
        // 어떤 소셜서비스든 그 서비스에서 각 계정마다의 유니크한 id값을 전달해주겠다
        // 구글은 sub, 네이버는 id 필드가 유니크 필드
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 데이터를 담을 클래스
        // attribute: {name, id, key, email, picture}
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 기존 회원이면 update, 신규 회원이면 save
        Gardener gardener = saveOrUpdate(oAuth2Attribute);

        // oAuth2User.getAttributes()로 가져오는 map은 수정 불가능한 맵
        // Gardener 테이블의 PK를 (username이 아니라) gardener_id로 잡고 있으므로
        // PK 값을 함께 Security Context에 저장하기 위해 평범한 map으로 변환
        Map<String, Object> attributes = oAuth2Attribute.toMap();
        attributes.put("gardenerId", gardener.getGardenerId());

        // UserPrincipal: Authentication에 담을 OAuth2User와 (일반 로그인 용)UserDetails를 implements한 커스텀 클래스
        return UserPrincipal.create(gardener, oAuth2Attribute.getAttributes());
    }

    /**
     * DB에 사용자 정보를 저장/수정한다
     * @param oAuth2Attribute 엔티티를 만들 정보들
     * @return
     */
    public Gardener saveOrUpdate(OAuth2Attribute oAuth2Attribute){
        Gardener gardener = gardenerDao.getGardenerByUsernameAndProvider(oAuth2Attribute.getEmail(), oAuth2Attribute.getProvider())
                .orElse(oAuth2Attribute.toEntity());

        return gardenerDao.save(gardener);
    }
}

package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.service.AuthenticationService;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final GardenerDao gardenerDao;
    private final JwtAuthTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;

    /**
     * 아이디 중복 검사
     *
     * @param username
     * @return 중복 아이디가 있으면 그 username을, 없을 시 null
     */
    @Override
    public String hasSameUsername(String username) {
        Optional<Gardener> gardener = gardenerDao.getGardenerByUsername(username);

        if (gardener.isEmpty()) {
            return null;
        }

        return gardener.get().getUsername();
    }

    @Override
    public GardenerDto.Info add(GardenerDto.Register register) {
        // DTO에 암호화된 비밀번호 저장한 뒤 엔티티로 변환
        Gardener gardener = gardenerDao.save(
                register
                        .encryptPassword(encoder.encode(register.getPassword()))
                        .toEntity()
        );

        return setAuthentication(gardener);
    }

    /**
     * GardenerDto의 ID, PW를 사용하여 DB를 통한 인증
     * 인증 성공 시 그 과정에서 받아온 Gardener 객체를 사용해 UserPrincipal을 만들고,
     * UsernamePasswordAuthenticationToken를 Security Context에 저장
     * 이후 JwtAuthToken을 생성 후 리턴한다.
     *
     * @param login
     * @return JwtAuthToken을 인코딩한 String 값 리턴
     */
    @Override
    public GardenerDto.Info login(GardenerDto.Login login) {
        Gardener gardener = gardenerDao.getGardenerForLogin(login.getUsername());

        // 비밀번호 일치 여부 검사
        if (!encoder.matches(login.getPassword(), gardener.getPassword())) {
            throw new BadCredentialsException("비밀번호 오류");
        }

        return setAuthentication(gardener);
    }

    // 인증 성공 후 Security Context에 유저 정보를 저장하고 토큰과 기본 정보를 리턴
    GardenerDto.Info setAuthentication(Gardener gardener) {
        // gardener 객체를 포함한 userPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(gardener);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // 인코딩된 값 리턴
        String jwtToken = tokenProvider.createAuthToken(userPrincipal).getToken();

        // return new GardenerInfo(jwtToken, gardener.getGardenerNo(), gardener.getName());
        return GardenerDto.Info.from(jwtToken, gardener);
    }
}

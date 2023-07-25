package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.ActiveGardener;
import com.buckwheat.garden.data.token.RefreshToken;
import com.buckwheat.garden.repository.RedisRepository;
import com.buckwheat.garden.service.AuthenticationService;
import com.buckwheat.garden.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final GardenerDao gardenerDao;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;
    private final RedisRepository redisRepository;

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

    @Override
    public GardenerDto.Info getGardenerInfo(Gardener gardener) {
        RefreshToken refreshToken = getRefreshToken(gardener);

        return GardenerDto.Info.from(null, refreshToken.getToken(), gardener);
    }

    // 인증 성공 후 Security Context에 유저 정보를 저장하고 토큰과 기본 정보를 리턴
    GardenerDto.Info setAuthentication(Gardener gardener) {
        // gardener 객체를 포함한 userPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(gardener);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // access token, expired date
        AccessToken accessToken = tokenProvider.createAccessToken(userPrincipal);
        RefreshToken refreshToken = getRefreshToken(gardener);

        return GardenerDto.Info.from(accessToken.getToken(), refreshToken.getToken(), gardener);
    }

    RefreshToken getRefreshToken(Gardener gardener){
        // refresh token
        RefreshToken refreshToken = RefreshToken.getRefreshToken();

        // redis에 저장
        redisRepository.save(ActiveGardener.from(gardener, refreshToken));
        log.debug("redis 조회 - findByGardenerId: {}", redisRepository.findById(gardener.getGardenerId()));

        return refreshToken;
    }

    @Override
    public GardenerDto.Token refreshToken(GardenerDto.Refresh token) {
        log.debug("request token: {}", token);

        String reqRefreshToken = token.getRefreshToken();

        log.debug("redis 전체 정보: {}", redisRepository.findAll());

        ActiveGardener activeGardener = redisRepository.findById(token.getGardenerId())
                .orElseThrow(NoSuchElementException::new);
        RefreshToken savedRefreshToken = activeGardener.getRefreshToken();

        if(!reqRefreshToken.equals(savedRefreshToken.getToken())){
            throw new BadCredentialsException("redis에 해당 사용자 없음");
        } else if(savedRefreshToken.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new BadCredentialsException("refresh token 만료");
        }

        // 새 access token 만들기
        Gardener gardener = gardenerDao.getGardenerByGardenerId(token.getGardenerId())
                .orElseThrow(NoSuchElementException::new);

        AccessToken accessToken = tokenProvider.createAccessToken(gardener);

        return new GardenerDto.Token(accessToken.getToken(), null);
    }
}

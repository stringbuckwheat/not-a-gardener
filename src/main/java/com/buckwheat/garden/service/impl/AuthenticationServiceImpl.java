package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.gardener.*;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.ActiveGardener;
import com.buckwheat.garden.data.token.RefreshToken;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.error.code.ExceptionCode;
import com.buckwheat.garden.repository.GardenerRepository;
import com.buckwheat.garden.repository.RedisRepository;
import com.buckwheat.garden.service.AuthenticationService;
import com.buckwheat.garden.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final GardenerRepository gardenerRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;
    private final RedisRepository redisRepository;

    @Override
    @Transactional(readOnly = true)
    public String hasSameUsername(String username) {
        Optional<Gardener> gardener = gardenerRepository.findByProviderIsNullAndUsername(username);
        return gardener.isEmpty() ? null : gardener.get().getUsername();
    }

    @Override
    @Transactional(readOnly = true)
    public Info getGardenerInfo(Long id) {
        Gardener gardener = gardenerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        ActiveGardener activeGardener = redisRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        RefreshToken refreshToken = activeGardener.getRefreshToken();

        return Info.from("", refreshToken.getToken(), gardener);
    }

    @Override
    @Transactional(readOnly = true)
    public Info login(Login login) {
        Gardener gardener = gardenerRepository.findByProviderIsNullAndUsername(login.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));

        // 비밀번호 일치 여부 검사
        if (!encoder.matches(login.getPassword(), gardener.getPassword())) {
            throw new BadCredentialsException(ExceptionCode.WRONG_PASSWORD.getCode());
        }

        return setAuthentication(gardener);
    }

    // 인증 성공 후 Security Context에 유저 정보를 저장하고 토큰과 기본 정보를 리턴
    public Info setAuthentication(Gardener gardener) {
        // gardener 객체를 포함한 user 생성
        UserPrincipal user = UserPrincipal.create(gardener);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // access token, expired date
        AccessToken accessToken = tokenProvider.createAccessToken(gardener.getGardenerId(), gardener.getName());
        RefreshToken refreshToken = tokenProvider.createRefreshToken(gardener.getGardenerId(), gardener.getName());

        return Info.from(accessToken.getToken(), refreshToken.getToken(), gardener);
    }

    @Override
    public Info add(Register register) {
        // DTO에 암호화된 비밀번호 저장한 뒤 엔티티로 변환
        Gardener gardener = gardenerRepository.save(
                register
                        .encryptPassword(encoder.encode(register.getPassword()))
                        .toEntity()
        );

        return setAuthentication(gardener);
    }

    @Override
    public Token refreshAccessToken(Refresh token) {
        String reqRefreshToken = token.getRefreshToken();
        ActiveGardener activeGardener = redisRepository.findById(token.getGardenerId())
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.NO_TOKEN_IN_REDIS.getCode()));
        RefreshToken savedRefreshToken = activeGardener.getRefreshToken();

        if (!reqRefreshToken.equals(savedRefreshToken.getToken())) {
            // redis의 refresh token과 일치하지 않음 -- B011
            redisRepository.deleteById(token.getGardenerId());
            throw new BadCredentialsException(ExceptionCode.INVALID_REFRESH_TOKEN.getCode());
        } else if (savedRefreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료 -- B002
            redisRepository.deleteById(token.getGardenerId());
            throw new BadCredentialsException(ExceptionCode.REFRESH_TOKEN_EXPIRED.getCode());
        }

        // 새 access token 만들기
        AccessToken accessToken = tokenProvider.createAccessToken(activeGardener.getGardenerId(), activeGardener.getName());

        // Refresh Token Rotation
        // Access token 재발급 시 Refresh Token도 재발급
        RefreshToken newRefreshToken = new RefreshToken();
        activeGardener.updateRefreshToken(newRefreshToken);
        redisRepository.save(activeGardener);

        return new Token(accessToken.getToken(), newRefreshToken.getToken());
    }

    @Override
    public void logOut(Long id) {
        redisRepository.deleteById(id);
    }
}

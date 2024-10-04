package xyz.notagardener.authentication.service;

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
import xyz.notagardener.authentication.dto.*;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;
import xyz.notagardener.authentication.token.AccessToken;
import xyz.notagardener.authentication.token.RefreshToken;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ExpiredRefreshTokenException;
import xyz.notagardener.common.error.exception.GardenerNotInSessionException;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;
    private final ActiveGardenerRepository activeGardenerRepository;
    private final GardenerRepository gardenerRepository;

    @Override
    @Transactional(readOnly = true)
    public Info getGardenerInfo(Long id) {
        Gardener gardener = gardenerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_SUCH_GARDENER));
        ActiveGardener activeGardener = activeGardenerRepository.findById(id)
                .orElseThrow(() -> new GardenerNotInSessionException(ExceptionCode.NO_TOKEN_IN_REDIS));

        return new Info("", activeGardener.getRefreshToken().getToken(), gardener);
    }

    @Override
    @Transactional
    public Info login(Login login) {
        Gardener gardener = gardenerRepository.findByProviderIsNullAndUsername(login.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.WRONG_ACCOUNT.getCode()));

        // 비밀번호 일치 여부 검사
        if (!encoder.matches(login.getPassword(), gardener.getPassword())) {
            throw new BadCredentialsException(ExceptionCode.WRONG_ACCOUNT.getCode());
        }

        gardener.updateRecentLogin();

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
        RefreshToken refreshToken = tokenProvider.createRefreshToken(gardener.getGardenerId(), gardener.getName(), gardener.getProvider());

        return new Info(accessToken.getToken(), refreshToken.getToken(), gardener);
    }

    @Override
    public AuthTokens refreshAccessToken(Refresh token) {
        String reqRefreshToken = token.getRefreshToken();
        ActiveGardener activeGardener = activeGardenerRepository.findById(token.getGardenerId())
                .orElseThrow(() -> new GardenerNotInSessionException(ExceptionCode.NO_TOKEN_IN_REDIS));
        RefreshToken savedRefreshToken = activeGardener.getRefreshToken();

        if (!reqRefreshToken.equals(savedRefreshToken.getToken())) {
            // redis의 refresh token과 일치하지 않음
            activeGardenerRepository.deleteById(token.getGardenerId());
            throw new BadCredentialsException(ExceptionCode.INVALID_REFRESH_TOKEN.getCode());
        } else if (savedRefreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            // refresh token 만료
            activeGardenerRepository.deleteById(token.getGardenerId());
            throw new ExpiredRefreshTokenException(ExceptionCode.REFRESH_TOKEN_EXPIRED);
        }

        // 새 access token 만들기
        AccessToken accessToken = tokenProvider.createAccessToken(activeGardener.getGardenerId(), activeGardener.getName());

        // Refresh AuthTokens Rotation
        // Access token 재발급 시 Refresh Token도 재발급
        RefreshToken newRefreshToken = new RefreshToken();
        activeGardener.updateRefreshToken(newRefreshToken);
        activeGardenerRepository.save(activeGardener);

        return new AuthTokens(accessToken.getToken(), newRefreshToken.getToken());
    }

    @Override
    public void logOut(Long id) {
        activeGardenerRepository.deleteById(id);
    }
}

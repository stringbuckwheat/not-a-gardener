package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.ActiveGardener;
import com.buckwheat.garden.data.token.RefreshToken;
import com.buckwheat.garden.repository.RedisRepository;
import com.buckwheat.garden.service.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 로그인 성공 시 부가작업
     * JWT 발급 후 token과 함께 리다이렉트
     * @param request 인증 성공된 리퀘스트
     * @param response
     * @param authentication Security Context의 Authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // 토큰 생성
        AccessToken accessToken = tokenProvider.createAccessToken(userPrincipal);
        RefreshToken refreshToken = RefreshToken.getRefreshToken();

        // Redis에 저장
        redisRepository.save(ActiveGardener.from(userPrincipal.getGardener(), refreshToken));

        // 리프레쉬 토큰 쿠키에 담기
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        response.addCookie(cookie);

        // 기본 정보 담기
        GardenerDto.SimpleInfo gardener = GardenerDto.SimpleInfo.from(userPrincipal.getGardener());
        String gardenerJson = objectMapper.writeValueAsString(gardener);
        response.getWriter().write(gardenerJson);

        String targetUrl = "http://not-a-gardener.xyz/oauth/" + accessToken.getToken();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

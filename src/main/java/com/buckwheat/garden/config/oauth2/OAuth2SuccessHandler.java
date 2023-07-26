package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.service.TokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final String TARGET_URL = "http://not-a-gardener.xyz/oauth/";

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
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        // access 토큰 생성
        AccessToken accessToken = tokenProvider.createAccessToken(user.getName(), user.getId());
        getRedirectStrategy().sendRedirect(request, response, TARGET_URL + accessToken.getToken());
    }
}

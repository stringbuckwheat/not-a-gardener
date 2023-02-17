package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.service.JwtAuthTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Override // 로그인 성공 시 부가작업을 하는 메소드
    // @ResponseBody
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // token 생성 및 발급 후 token과 함께 리다이렉트
        log.debug("Authentication: {}", authentication);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.debug("principal에서 꺼낸 OAuth2User: {}", oAuth2User);
        log.debug("토큰 발행 시작");

        // claims 만들기
        Map<String, String> claims = new HashMap<>();

        String id = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");
        claims.put("id", id);
        claims.put("name", name);

        log.debug("name: {}", name);

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());

        String targetUrl = "http://localhost:3000/oauth/"
                + jwtAuthTokenProvider.createAuthToken(id, claims, expiredDate).getToken();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

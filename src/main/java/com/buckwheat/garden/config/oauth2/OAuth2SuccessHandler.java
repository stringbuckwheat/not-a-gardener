package com.buckwheat.garden.config.oauth2;

import com.buckwheat.garden.data.token.JwtAuthToken;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

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
    private final ObjectMapper objectMapper;

    @Override // 로그인 성공 시 부가작업을 하는 메소드
    @ResponseBody
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 최초 로그인인지 확인
        // Access/Refresh token 생성 및 발급
        // token을 포함하여 리다이렉트

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Name: [stringbuckwheat@gmail.com],
        // Granted Authorities: [[ROLE_USER]],
        // User Attributes: [{name=최서경, id=sub, key=sub, email=stringbuckwheat@gmail.com, picture=https://lh3.googleusercontent.com/a/AEdFTp6LDv3ehxMKkXSnPuHZDM9vQrdwJtEdJfFclfIn=s96-c}]
        log.debug("principal에서 꺼낸 OAuth2User: {}", oAuth2User);
        log.debug("토큰 발행 시작");

        // claims 만들기
        Map<String, String> claims = new HashMap<>();

        String id = (String) oAuth2User.getAttributes().get("email");
        claims.put("id", id);
        claims.put("name", (String) oAuth2User.getAttributes().get("name"));

        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());

        JwtAuthToken token = jwtAuthTokenProvider.createAuthToken(id, claims, expiredDate);
        log.debug("token: {}", token.getData());

        String targetUrl = "http://localhost:3000/oauth/" + token.getToken();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

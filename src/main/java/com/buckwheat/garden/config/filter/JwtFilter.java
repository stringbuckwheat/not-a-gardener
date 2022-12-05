package com.buckwheat.garden.config.filter;

import com.buckwheat.garden.data.dto.JwtAuthToken;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    // 로그인 이후 토큰 자체에 대한 검증
    public static final String AUTHORIZATION_HEADER = "x-auth-token";
    private JwtAuthTokenProvider tokenProvider;

    public JwtFilter(JwtAuthTokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    private Optional<String> resolveToken(HttpServletRequest request){
        // Request의 Header에 담긴 토큰 값을 가져온다
        // x-auth-token: 토큰값 형태로 저장
        String authToken = request.getHeader(AUTHORIZATION_HEADER);

        // 공백 혹은 null이 아니면
        if(StringUtils.hasText(authToken)){
            return Optional.of(authToken);
        } else {
            return Optional.empty();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("*** JWT FILTER ***");

        Optional<String> token = resolveToken(request);

        // Optional 안의 객체가 null이 아니면
        if(token.isPresent()){
            // header의 token로 token, key를 포함하는 새로운 JwtAuthToken 만들기
            JwtAuthToken jwtAuthToken = (JwtAuthToken) tokenProvider.convertAuthToken(token.get());

            // boolean validate() -> getData(): claims or null
            // 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
            if(jwtAuthToken.validate()){
                // UsernamePasswordAuthenticationToken(유저, authToken, 권한)
                Authentication authentication = tokenProvider.getAuthentication(jwtAuthToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}

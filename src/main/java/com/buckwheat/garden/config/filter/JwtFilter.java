package com.buckwheat.garden.config.filter;

import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.service.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
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

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // 로그인 이후 토큰 자체에 대한 검증
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    private String resolveToken(HttpServletRequest request){
        // Request의 Header에 담긴 토큰 값을 가져온다

        String token = request.getHeader(AUTHORIZATION_HEADER);

        // 공백 혹은 null이 아니고 Bearer로 시작하면
        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)){
            // Bearer 떼고 줌
            return token.split(" ")[1].trim();
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("*** JWT FILTER ***");
        log.debug("주소: {}", request.getRequestURL());

        String token = resolveToken(request);

        try{
            // 디코딩할만한 토큰이 왔으면
            if(token != null){
                // header의 token로 token, key를 포함하는 새로운 JwtAuthToken 만들기
                AccessToken accessToken = tokenProvider.convertAuthToken(token);

                // boolean validate() -> getData(): claims or null
                // 정상 토큰이면 해당 토큰으로 Authentication을 가져와서 SecurityContext에 저장
                if(accessToken.validate()){
                    // UsernamePasswordAuthenticationToken(유저, authToken, 권한)
                    Authentication authentication = tokenProvider.getAuthentication(accessToken);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            log.debug("토큰 만료");
            throw new JwtException("토큰 기한 만료");
        } catch(SecurityException e){
            log.info("Invalid JWT signature.");
        } catch(MalformedJwtException e){
            log.info("Invalid JWT token.");
        } catch(UnsupportedJwtException e){
            log.info("Unsupported JWT token.");
        } catch(IllegalArgumentException e){
            log.info("JWT token compact of handler are invalid");
        }


        filterChain.doFilter(request, response);
    }
}

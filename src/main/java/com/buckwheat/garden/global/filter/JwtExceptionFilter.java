package com.buckwheat.garden.global.filter;

import com.buckwheat.garden.global.error.ErrorResponse;
import com.buckwheat.garden.global.error.code.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response); // go to 'JwtAuthenticationFilter'
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.ACCESS_TOKEN_EXPIRED));
        } catch (MalformedJwtException e) {
            // 토큰 값이 올바르지 않을 때
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.INVALID_JWT_TOKEN));
        } catch (JwtException | SecurityException e) {
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.CANNOT_LOGIN));
        } catch (UsernameNotFoundException e) {
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.REFRESH_TOKEN_EXPIRED));
        }
    }

    public void setErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

package com.buckwheat.garden.config.filter;

import com.buckwheat.garden.error.ErrorResponse;
import com.buckwheat.garden.error.code.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            log.info("Invalid JWT token");
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.INVALID_JWT_TOKEN));
        } catch (JwtException | SecurityException | IllegalArgumentException e){
            setErrorResponse(response, ErrorResponse.from(ExceptionCode.INVALID_JWT_TOKEN));
        }
    }

    public void setErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

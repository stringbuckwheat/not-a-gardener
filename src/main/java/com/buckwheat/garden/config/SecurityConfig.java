package com.buckwheat.garden.config;

import com.buckwheat.garden.filter.JwtExceptionFilter;
import com.buckwheat.garden.filter.JwtFilter;
import com.buckwheat.garden.filter.oauth2.OAuth2MemberService;
import com.buckwheat.garden.filter.oauth2.OAuth2SuccessHandler;
import com.buckwheat.garden.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity //(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Value("${origin}")
    private String allowedOrigin;

    /**
     * CORS 설정
     */
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));

            config.setAllowedOriginPatterns(Collections.singletonList(allowedOrigin));
            config.setAllowCredentials(true);

            return config;
        };
    }

    /**
     * Security Filter Chain 커스터마이징
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/login", "/api/token", "/api/oauth", "/api/register/**", "/api/forgot/**", "/api/logout").permitAll() // MEMIL 모든 접속 허용

                                .requestMatchers("/api/chemical/**", "/api/garden/**", "/api/gardener/**", "/api/goal/**", "/api/info",
                                        "/api/place/**", "/api/plant/**", "/api/routine/**", "/api/watering/**").authenticated()
                )
                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .successHandler(successHandler)
                        .userInfoEndpoint(configurer -> configurer.userService(oAuth2MemberService))
                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);

        return httpSecurity.build();
    }

    /**
     * 비밀번호 암호화 및 일치 여부 확인에 사용
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder encodePw(){
        return new BCryptPasswordEncoder();
    }

}
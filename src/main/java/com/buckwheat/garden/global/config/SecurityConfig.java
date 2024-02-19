package com.buckwheat.garden.global.config;

import com.buckwheat.garden.global.filter.CustomAuthenticationEntryPoint;
import com.buckwheat.garden.global.filter.JwtExceptionFilter;
import com.buckwheat.garden.global.filter.JwtFilter;
import com.buckwheat.garden.global.filter.oauth2.OAuth2MemberService;
import com.buckwheat.garden.global.filter.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler successHandler;
    private final JwtFilter jwtFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    @Value("${origin}")
    private String allowedOrigin;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/api/login", "/api/token", "/api/oauth", "/api/register/**", "/api/forgot/**", "/api/logout", "/static/**"
    };

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
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/chemical/**", "/api/garden/**", "/api/gardener/**", "/api/goal/**", "/api/info",
                                        "/api/place/**", "/api/plant/**", "/api/routine/**", "/api/watering/**").authenticated()
                                .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint))

                .oauth2Login(oauth2Configurer -> oauth2Configurer
                        .loginPage("/")
                        .successHandler(successHandler)
                        .userInfoEndpoint(configurer -> configurer.userService(oAuth2MemberService))
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtFilter.class);

        return httpSecurity.build();
    }

    /**
     * 비밀번호 암호화 및 일치 여부 확인에 사용
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder encodePw() {
        return new BCryptPasswordEncoder();
    }

}
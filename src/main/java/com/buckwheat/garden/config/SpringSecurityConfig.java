package com.buckwheat.garden.config;

import com.buckwheat.garden.config.filter.JwtFilter;
import com.buckwheat.garden.config.oauth2.OAuth2MemberService;
import com.buckwheat.garden.config.oauth2.OAuth2SuccessHandler;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity //(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SpringSecurityConfig {
    private final JwtAuthTokenProvider tokenProvider;
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler successHandler;

    /**
     * 리액트 서버와 통신하기 위해 CORS 문제 해결
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 요청에 credential 권한이 있는지 없는지
        // Authorization으로 사용자 인증 시 true
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // 요청 권한을 줄 도메인
        config.addAllowedHeader("*"); // 노출해도 되는 헤더

        // 허용할 메소드.
        // 특정 메소드만 허용하려면 HttpMethod.GET 식으로 추가
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    /**
     * Security Filter Chain 커스터마이징
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable() // 로그인 인증창 해제
                .csrf().disable() // REST Api이므로 csrf disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 토큰 인증이므로 세션은 stateless

                .and()
                .authorizeRequests() // 리퀘스트 설정
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Preflight 요청 허용
                .antMatchers("/", "/oauth", "/register").permitAll() // 누구나 접근가능
                .antMatchers("/garden/**").authenticated() // 인증 권한 필요

                .and()
                .addFilter(this.corsFilter()) // CORS 필터 등록
                // 기본 인증 필터인 UsernamePasswordAuthenticationFilter 대신 Custom 필터 등록
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                // OAuth2 로그인 설정
                .oauth2Login().loginPage("/")
                // 성공 시 수행할 핸들러
                .successHandler(successHandler)
                // OAuth2 로그인 성공 이후 설정
                .userInfoEndpoint().userService(oAuth2MemberService);

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

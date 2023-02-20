package com.buckwheat.garden.config;

import com.buckwheat.garden.config.filter.JwtFilter;
import com.buckwheat.garden.config.oauth2.OAuth2MemberService;
import com.buckwheat.garden.config.oauth2.OAuth2SuccessHandler;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
@EnableWebSecurity//(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SpringSecurityConfig {
    private final JwtAuthTokenProvider tokenProvider;
    private final OAuth2MemberService oAuth2MemberService;
    private final OAuth2SuccessHandler successHandler;

    @Bean
    public CorsFilter corsFilter() {
        log.debug("*** CORS filter ***");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/", "/oauth", "/register").permitAll()
                .antMatchers("/garden/**").authenticated()
                // .anyRequest().hasRole("USER")

                .and()
                .addFilter(this.corsFilter()) // ** CorsFilter 등록 **
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .oauth2Login().loginPage("/")
                .successHandler(successHandler)
                .userInfoEndpoint().userService(oAuth2MemberService);

        return httpSecurity.build();
    }

    // 해당 메서드에서 리턴되는 객체를 빈으로 등록
    @Bean
    public BCryptPasswordEncoder encodePw(){
        return new BCryptPasswordEncoder();
    }

}

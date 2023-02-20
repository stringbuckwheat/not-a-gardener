package com.buckwheat.garden.service;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.token.JwtAuthToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Map;

public interface JwtAuthTokenProvider {
    JwtAuthToken convertAuthToken(String token);
    JwtAuthToken createAuthToken(UserPrincipal userPrincipal);
    Authentication getAuthentication(JwtAuthToken authToken);
}

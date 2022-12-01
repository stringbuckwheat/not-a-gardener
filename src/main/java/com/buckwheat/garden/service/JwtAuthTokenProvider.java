package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.JwtAuthToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Map;

public interface JwtAuthTokenProvider {
    JwtAuthToken createAuthToken(String id, Map<String, String> claims, Date expiredDate);
    JwtAuthToken convertAuthToken(String token);
    Authentication getAuthentication(JwtAuthToken authToken);
}

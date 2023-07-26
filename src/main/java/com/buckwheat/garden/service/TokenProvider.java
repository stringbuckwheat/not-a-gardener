package com.buckwheat.garden.service;

import com.buckwheat.garden.data.token.AccessToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);
    AccessToken createAccessToken(String name, Long id);
    Authentication getAuthentication(AccessToken authToken);
}

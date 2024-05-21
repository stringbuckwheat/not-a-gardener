package xyz.notagardener.authentication.service;

import org.springframework.security.core.Authentication;
import xyz.notagardener.authentication.dto.AccessToken;
import xyz.notagardener.authentication.dto.RefreshToken;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);

    AccessToken createAccessToken(Long gardenerId, String name);

    Authentication getAuthentication(AccessToken authToken);

    RefreshToken createRefreshToken(Long gardenerId, String name, String provider);
}

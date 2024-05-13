package xyz.notagardener.domain.gardener.service;

import xyz.notagardener.domain.gardener.token.AccessToken;
import xyz.notagardener.domain.gardener.token.RefreshToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);

    AccessToken createAccessToken(Long gardenerId, String name);

    Authentication getAuthentication(AccessToken authToken);

    RefreshToken createRefreshToken(Long gardenerId, String name);
}

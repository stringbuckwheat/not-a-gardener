package com.buckwheat.garden.domain.gardener.service;

import com.buckwheat.garden.domain.gardener.token.AccessToken;
import com.buckwheat.garden.domain.gardener.token.RefreshToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);

    AccessToken createAccessToken(Long gardenerId, String name);

    Authentication getAuthentication(AccessToken authToken);

    RefreshToken createRefreshToken(Long gardenerId, String name);
}

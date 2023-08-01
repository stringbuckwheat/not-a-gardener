package com.buckwheat.garden.service;

import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.RefreshToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);
    AccessToken createAccessToken(String name, Long id);
    Authentication getAuthentication(AccessToken authToken);
    RefreshToken getRefreshToken(Gardener gardener);
    RefreshToken getRefreshToken(Long gardenerId, String name);
}

package com.buckwheat.garden.service;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.data.token.AccessToken;
import org.springframework.security.core.Authentication;

public interface TokenProvider {
    AccessToken convertAuthToken(String token);
    AccessToken createAccessToken(UserPrincipal userPrincipal);
    AccessToken createAccessToken(Gardener gardener);
    Authentication getAuthentication(AccessToken authToken);
}

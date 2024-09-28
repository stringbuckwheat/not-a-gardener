package xyz.notagardener.authentication.service;

import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.dto.Refresh;
import xyz.notagardener.authentication.dto.AuthTokens;
import xyz.notagardener.gardener.model.Gardener;

public interface AuthenticationService {
    Info getGardenerInfo(Long id);

    Info setAuthentication(Gardener gardener);
    Info login(Login login);

    void logOut(Long id);

    AuthTokens refreshAccessToken(Refresh token);
}

package xyz.notagardener.authentication.service;

import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.dto.Refresh;
import xyz.notagardener.authentication.dto.Token;
import xyz.notagardener.gardener.gardener.Register;

public interface AuthenticationService {
    String hasSameUsername(String username);

    Info getGardenerInfo(Long id);

    Info add(Register register);

    Info login(Login login);

    void logOut(Long id);

    Token refreshAccessToken(Refresh token);
}

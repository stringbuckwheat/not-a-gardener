package xyz.notagardener.gardener.authentication;

import xyz.notagardener.gardener.authentication.dto.*;

public interface AuthenticationService {
    String hasSameUsername(String username);

    Info getGardenerInfo(Long id);

    Info add(Register register);

    Info login(Login login);

    void logOut(Long id);

    Token refreshAccessToken(Refresh token);
}

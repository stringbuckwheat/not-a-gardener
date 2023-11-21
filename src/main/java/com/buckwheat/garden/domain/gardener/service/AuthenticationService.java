package com.buckwheat.garden.domain.gardener.service;

import com.buckwheat.garden.domain.gardener.dto.*;

public interface AuthenticationService {
    String hasSameUsername(String username);

    Info getGardenerInfo(Long id);

    Info add(Register register);

    Info login(Login login);

    void logOut(Long id);

    Token refreshAccessToken(Refresh token);
}

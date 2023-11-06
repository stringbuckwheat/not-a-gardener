package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.gardener.*;

public interface AuthenticationService {
    String hasSameUsername(String username);
    Info getGardenerInfo(Long id);
    Info add(Register register);
    Info login(Login login);
    void logOut(Long id);
    Token refreshAccessToken(Refresh token);
}

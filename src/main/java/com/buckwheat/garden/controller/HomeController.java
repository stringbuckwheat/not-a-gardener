package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.gardener.Info;
import com.buckwheat.garden.data.dto.gardener.Login;
import com.buckwheat.garden.data.dto.gardener.Refresh;
import com.buckwheat.garden.data.dto.gardener.Token;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public Info login(@RequestBody Login login) {
        return authenticationService.login(login);
    }

    @PostMapping("/token")
    public Token refreshToken(@RequestBody Refresh refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }

    @PostMapping("/logout/{gardenerId}")
    public void logOut(@PathVariable Long gardenerId) {
        authenticationService.logOut(gardenerId);
    }

    @GetMapping("/info")
    public Info getGardenerInfo(@AuthenticationPrincipal UserPrincipal user) {
        return authenticationService.getGardenerInfo(user.getId());
    }
}

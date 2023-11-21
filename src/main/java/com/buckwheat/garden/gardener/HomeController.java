package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.Info;
import com.buckwheat.garden.gardener.gardener.Login;
import com.buckwheat.garden.gardener.gardener.Refresh;
import com.buckwheat.garden.gardener.gardener.Token;
import com.buckwheat.garden.gardener.token.UserPrincipal;
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

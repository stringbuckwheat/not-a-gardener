package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenerDto;
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

    /**
     * 로그인
     * @param login id, pw
     * @return JWT 토큰, GardenerId, 이름이 포함된 GardenerInfo
     */
    @PostMapping("/login")
    public GardenerDto.Info login(@RequestBody GardenerDto.Login login){
        return authenticationService.login(login);
    }

    @PostMapping("/token")
    public GardenerDto.Token refreshToken(@RequestBody GardenerDto.Refresh refreshToken){
        return authenticationService.refreshAccessToken(refreshToken);
    }

    /* 간단한 회원 정보(헤더) - 소셜로그인에서 사용 */
    @GetMapping("/info")
    public GardenerDto.Info getGardenerInfo(@AuthenticationPrincipal UserPrincipal user){
        return authenticationService.getGardenerInfo(user.getId());
    }
}

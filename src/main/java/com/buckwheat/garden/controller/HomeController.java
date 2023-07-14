package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final AuthenticationService authenticationService;

    /**
     * 로그인
     * @param login id, pw
     * @return JWT 토큰, GardenerNo, 이름이 포함된 GardenerInfo
     */
    @PostMapping("/api/login")
    public GardenerDto.Info login(@RequestBody GardenerDto.Login login){
        return authenticationService.login(login);
    }
}

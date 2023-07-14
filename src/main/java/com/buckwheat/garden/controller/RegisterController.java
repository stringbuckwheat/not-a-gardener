package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final AuthenticationService authenticationService;

    /* 아이디 중복 검사 */
    @GetMapping("/username/{username}")
    public String hasSameUsername(@PathVariable String username){
        return authenticationService.hasSameUsername(username);
    }

    /* 회원 가입 */
    @PostMapping("")
    public GardenerDto.Info addGardener(@RequestBody GardenerDto.Register register){
        return authenticationService.add(register);
    }
}

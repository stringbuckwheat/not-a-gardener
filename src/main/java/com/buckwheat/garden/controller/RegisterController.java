package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final AuthenticationService authenticationService;

    /* 아이디 중복 검사 */
    @GetMapping("/username/{username}")
    public String checkUsername(@PathVariable String username){
        return authenticationService.getIdByInputId(username);
    }

    /* 회원 가입 */
    @PostMapping("")
    public MemberDto.MemberInfo addMember(@RequestBody MemberDto.RegisterDto registerDto){
        return authenticationService.addMember(registerDto);
    }
}

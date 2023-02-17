package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.service.MemberService;
import com.buckwheat.garden.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;

    /* 회원 가입 */
    @PostMapping("")
    public RegisterDto register(@RequestBody RegisterDto registerDto){
        return registerService.addMember(registerDto);
    }

    /* 아이디 중복 검사 */
    @PostMapping("/idCheck")
    public String idCheck(@RequestBody RegisterDto registerDto){
        return registerService.getIdByInputId(registerDto.getUsername());
    }

    /* 이메일 중복 검사 */
    @PostMapping("/emailCheck")
    public String emailCheck(@RequestBody RegisterDto registerDto){
        log.debug("email check");
        return registerService.getEmailByInputEmail(registerDto.getEmail());
    }
}

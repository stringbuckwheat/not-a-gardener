package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final LoginService loginService;

    /* 로그인 */
    // 소셜 로그인은 Security Filter에서 처리
    @PostMapping("")
    public String login(@RequestBody MemberDto memberDto){
        return loginService.login(memberDto);
    }

    /* TODO 아이디/비밀번호 찾기 */
    // 메일 보내서 코드 확인
}

package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.MemberInfo;
import com.buckwheat.garden.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    private final LoginService loginService;

    /**
     * 로그인
     * @param memberDto id, pw
     * @return JWT 토큰, memberNo, 이름이 포함된 MemberInfo
     */
    @PostMapping("")
    public MemberInfo login(@RequestBody MemberDto memberDto){
        return loginService.login(memberDto);
    }

    /* TODO 아이디/비밀번호 찾기 */
    // 메일 보내서 코드 확인
}

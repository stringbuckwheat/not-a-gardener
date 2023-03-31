package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDto;
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
     * @return JWT 토큰, memberNo, 이름이 포함된 MemberInfo
     */
    @PostMapping("")
    public MemberDto.MemberInfo login(@RequestBody MemberDto.Login login){
        return authenticationService.login(login);
    }

    /* TODO 아이디/비밀번호 찾기 */
    // 메일 보내서 코드 확인
}

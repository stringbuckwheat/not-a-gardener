package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDetailDto;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;


@Slf4j
@RestController
public class HomeController {
    @Autowired
    private MemberService memberService;

    /* 로그인 */
    @PostMapping("/")
    public String login(@RequestBody MemberDto memberDto){
        return memberService.login(memberDto);
    }

    /* TODO 아이디/비밀번호 찾기 */
    // 메일 보내서 코드 확인
}

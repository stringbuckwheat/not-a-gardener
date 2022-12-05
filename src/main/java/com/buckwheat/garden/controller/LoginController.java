package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/")
    public String login(@RequestBody MemberDto memberDto){
        log.debug("login() 메소드 호출");
        log.debug("member: " + memberDto);

        String token = loginService.login(memberDto);
        log.debug("final token: " + token);

//        return new ResponseEntity<>(Response.builder()
//                .response(Result.builder.token(token).build())
//                .content("success")
//                .build(), HttpStatus.OK
//        );
        return token;
    }

    @PostMapping("/register")
    public RegisterDto register(@RequestBody RegisterDto registerDto){
        log.debug("RegisterDto: " + registerDto);
        return loginService.addMember(registerDto);
    }

    @PostMapping("/idCheck")
    public String idCheck(@RequestBody RegisterDto registerDto){
        log.debug("idcheck: " + registerDto);

        return loginService.getIdByInputId(registerDto.getId());
    }
}

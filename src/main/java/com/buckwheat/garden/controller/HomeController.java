package com.buckwheat.garden.controller;

import com.buckwheat.garden.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class HomeController {

    @PostMapping("/")
    public Member login(@RequestBody Member member){
        log.debug("login() 메소드 호출");
        log.debug("member: " + member);

        return member;
    }
}

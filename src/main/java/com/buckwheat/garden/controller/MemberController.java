package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor // 어떠한 빈(Bean)에 생성자가 오직 하나만 있고, 생성자의 파라미터 타입이 빈으로 등록 가능한 존재라면 이 빈은 @Autowired 어노테이션 없이도 의존성 주입이 가능하다.
public class MemberController {
    private final MemberService memberService;

    /* 회원정보 보기 */
    @GetMapping("/{memberNo}")
    public MemberDto.Detail getMember(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return memberService.getMember(userPrincipal.getMember());
    }

    /* 간단한 회원 정보(헤더) - 소셜로그인에서 사용 */
    @GetMapping("/member-info")
    public MemberDto.Info getMemberInfo(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return MemberDto.Info.from(null, userPrincipal.getMember());
    }

    /* 이메일로 계정 확인 - 아이디/비밀번호 찾기 */
    @GetMapping("/email/{email}")
    public Map<String, Object> sendAuthenticationEmail (@PathVariable String email){
        return memberService.getIdentificationCodeAndMembers(email);
    }

    /* 비밀번호 변경 전 한 번 입력받아서 확인 */
    @PostMapping("/pw")
    public boolean reconfirmPassword(@RequestBody MemberDto.Login login, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return memberService.identifyMember(login, userPrincipal.getMember());
    }

    /* 로그인 후 비밀번호 변경 */
    @PutMapping("/pw")
    public void updatePassword(@RequestBody MemberDto.Login login, @AuthenticationPrincipal UserPrincipal userPrincipal){
        memberService.updatePassword(login, userPrincipal.getMember());
    }

    @PutMapping("/{username}/pw")
    public void resetPassword(@RequestBody MemberDto.Login login){
        memberService.resetPassword(login);
    }

    /* 회원정보 변경 */
    @PutMapping("/{memberNo}")
    public MemberDto.Detail updateMember(@RequestBody MemberDto.Detail memberDetailDto, @PathVariable int memberNo){
        return memberService.updateMember(memberDetailDto);
    }

    /* 탈퇴 */
    @DeleteMapping("/{memberNo}")
    public void deleteMember(@PathVariable("memberNo") int memberNo){
        memberService.removeMember(memberNo);
    }
}

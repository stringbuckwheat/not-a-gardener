package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.MemberDetailDto;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.MemberInfo;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor // 어떠한 빈(Bean)에 생성자가 오직 하나만 있고, 생성자의 파라미터 타입이 빈으로 등록 가능한 존재라면 이 빈은 @Autowired 어노테이션 없이도 의존성 주입이 가능하다.
public class MemberController {
    private final MemberService memberService;

    /* 회원정보 보기 */
    @GetMapping("/{memberNo}")
    public MemberDetailDto getMember(@PathVariable("memberNo") int memberNo){
        return memberService.getMember(memberNo);
    }

    @GetMapping("/member-info")
    public MemberInfo getMemberInfo(@AuthenticationPrincipal UserPrincipal userPrincipal){
        Member member = userPrincipal.getMember();
        return MemberInfo.getMemberInfo(member.getMemberNo(), member.getName());
    }

    /* 비밀번호 변경 전 한 번 입력받아서 확인 */
    @PostMapping("")
    public boolean reconfirmPassword(@RequestBody MemberDto memberDto, @AuthenticationPrincipal User user){
        memberDto.setUsername(user.getUsername());
        return memberService.identifyMember(memberDto);
    }

    /* 비밀번호 변경 */
    @PutMapping("/updatePw")
    public void updatePassword(@RequestBody MemberDto memberDto){
        // @PathVariable을 쓰는 게 나은지, @AuthenticationPrincipal을 쓰는 게 나은지
        memberService.updatePassword(memberDto);
    }

    /* 회원정보 변경 */
    @PutMapping("/{username}")
    public void updateMember(@RequestBody RegisterDto registerDto){
        memberService.updateMember(registerDto);
    }

    /* 탈퇴 */
    @DeleteMapping("/{username}")
    public void deleteMember(@PathVariable("username") String username){
        memberService.removeMember(username);
    }

}

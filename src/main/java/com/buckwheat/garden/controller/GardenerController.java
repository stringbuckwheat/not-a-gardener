package com.buckwheat.garden.controller;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.service.GardenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/gardener")
@RequiredArgsConstructor // 어떠한 빈(Bean)에 생성자가 오직 하나만 있고, 생성자의 파라미터 타입이 빈으로 등록 가능한 존재라면 이 빈은 @Autowired 어노테이션 없이도 의존성 주입이 가능하다.
public class GardenerController {
    private final GardenerService gardenerService;

    /* 회원정보 보기 */
    @GetMapping("/{gardenerId}")
    public GardenerDto.Detail getGardener(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenerService.getGardenerDetail(userPrincipal.getGardener());
    }

    /* 이메일로 계정 확인 - 아이디/비밀번호 찾기 */
    @GetMapping("/email/{email}")
    public Map<String, Object> forgotAccount (@PathVariable String email){
        return gardenerService.forgotAccount(email);
    }

    /* 비밀번호 변경 전 한 번 입력받아서 확인 */
    @PostMapping("/password")
    public boolean identify(@RequestBody GardenerDto.Login login, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return gardenerService.identify(userPrincipal.getGardener(), login);
    }

    /* 로그인 후 비밀번호 변경 */
    @PutMapping("/password")
    public void updatePassword(@RequestBody GardenerDto.Login login, @AuthenticationPrincipal UserPrincipal userPrincipal){
        gardenerService.updatePassword(userPrincipal.getGardener(), login);
    }

    @PutMapping("/{username}/password")
    public void resetPassword(@RequestBody GardenerDto.Login login){
        gardenerService.resetPassword(login);
    }

    /* 회원정보 변경 */
    @PutMapping("/{gardenerId}")
    public GardenerDto.Detail modify(@RequestBody GardenerDto.Detail gardenerDetail, @PathVariable long gardenerId){
        return gardenerService.modify(gardenerDetail);
    }

    /* 탈퇴 */
    @DeleteMapping("/{gardenerId}")
    public void deleteGardener(@PathVariable("gardenerId") long gardenerId){
        gardenerService.delete(gardenerId);
    }
}

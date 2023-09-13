package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.service.GardenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/gardener")
@RequiredArgsConstructor
// 어떠한 빈(Bean)에 생성자가 오직 하나만 있고, 생성자의 파라미터 타입이 빈으로 등록 가능한 존재라면 이 빈은 @Autowired 어노테이션 없이도 의존성 주입이 가능하다.
public class GardenerController {
    private final GardenerService gardenerService;

    /* 회원정보 보기 */
    @GetMapping("/{gardenerId}")
    public GardenerDto.Detail getOne(@AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.getOne(user.getId());
    }

    /* 비밀번호 변경 전 한 번 입력받아서 확인 */
    @PostMapping("/password")
    public boolean identify(@RequestBody GardenerDto.Login login, @AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.identify(user.getId(), login);
    }

    /* 로그인 후 비밀번호 변경 */
    @PutMapping("/password")
    public void updatePassword(@RequestBody GardenerDto.Login login, @AuthenticationPrincipal UserPrincipal user) {
        gardenerService.updatePassword(user.getId(), login);
    }

    /* 회원정보 변경 */
    @PutMapping("/{gardenerId}")
    public GardenerDto.Detail modify(@RequestBody GardenerDto.Detail gardenerDetail, @PathVariable long gardenerId) {
        return gardenerService.modify(gardenerDetail);
    }

    /* 탈퇴 */
    @DeleteMapping("/{gardenerId}")
    public void deleteGardener(@PathVariable("gardenerId") long gardenerId) {
        gardenerService.delete(gardenerId);
    }
}

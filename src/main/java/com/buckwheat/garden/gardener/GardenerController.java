package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.GardenerDetail;
import com.buckwheat.garden.gardener.gardener.Login;
import com.buckwheat.garden.gardener.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/gardener")
@RequiredArgsConstructor
public class GardenerController {
    private final GardenerService gardenerService;

    @GetMapping("/{gardenerId}")
    public GardenerDetail getOne(@AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.getOne(user.getId());
    }

    /* 비밀번호 변경 전 한 번 입력받아서 확인 */
    @PostMapping("/password")
    public boolean identify(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        return gardenerService.identify(user.getId(), login);
    }

    @PutMapping("/password")
    public void updatePassword(@RequestBody Login login, @AuthenticationPrincipal UserPrincipal user) {
        gardenerService.updatePassword(user.getId(), login);
    }

    @PutMapping("/{gardenerId}")
    public GardenerDetail update(@RequestBody GardenerDetail gardenerDetail, @PathVariable long gardenerId) {
        return gardenerService.update(gardenerDetail);
    }

    @DeleteMapping("/{gardenerId}")
    public void delete(@PathVariable("gardenerId") long gardenerId) {
        gardenerService.delete(gardenerId);
    }
}

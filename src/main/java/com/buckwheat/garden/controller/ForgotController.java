package com.buckwheat.garden.controller;

import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.service.ForgotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forgot")
public class ForgotController {
    private final ForgotService forgotService;

    /* 이메일로 계정 확인 - 아이디/비밀번호 찾기 */
    @GetMapping("/email/{email}")
    public GardenerDto.Forgot forgotAccount(@PathVariable String email) {
        return forgotService.forgotAccount(email);
    }

    @PutMapping("/{username}/password")
    public void resetPassword(@RequestBody GardenerDto.Login login) {
        forgotService.resetPassword(login);
    }
}

package com.buckwheat.garden.gardener;

import com.buckwheat.garden.gardener.gardener.Forgot;
import com.buckwheat.garden.gardener.gardener.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forgot")
public class ForgotController {
    private final ForgotService forgotService;

    @GetMapping("/email/{email}")
    public Forgot forgotAccount(@PathVariable String email) {
        return forgotService.forgotAccount(email);
    }

    @PutMapping("/{username}/password")
    public void resetPassword(@RequestBody Login login) {
        forgotService.resetPassword(login);
    }
}

package com.buckwheat.garden.domain.gardener.controller;

import com.buckwheat.garden.domain.gardener.service.AuthenticationService;
import com.buckwheat.garden.domain.gardener.dto.Info;
import com.buckwheat.garden.domain.gardener.dto.Register;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class RegisterController {
    private final AuthenticationService authenticationService;

    @GetMapping("/username/{username}")
    public String hasSameUsername(@PathVariable String username) {
        return authenticationService.hasSameUsername(username);
    }

    @PostMapping("")
    public Info add(@RequestBody Register register) {
        return authenticationService.add(register);
    }
}
